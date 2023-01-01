package com.temp.ggtest.data

import android.content.Context
import com.squareup.moshi.Moshi
import com.temp.ggtest.data.model.Hit
import com.temp.ggtest.data.model.Hits
import dagger.hilt.android.qualifiers.ApplicationContext
import com.temp.ggtest.data.net.Api
import com.temp.ggtest.data.net.Api.Companion.apiCall
import com.temp.ggtest.data.persist.HitDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * This singleton handles all data requests as a single source using flow,
 * automatically fetches database and network data without caller engagement
 *
 * @param api retrofit api instance
 * @param dao room database dao instance
 */
@Singleton
class DataSource @Inject constructor(
    private val api: Api,
    private val dao: HitDao
) {
    /**
     * Get hits from a network call
     * @return Flow that will update when data is available
     */
    fun getHits(): Flow<CallResult<List<Hit>>> {
        return getFlow(
            { dao.loadHits() },
            { apiCall { api.getHits() } })
        { hits, result, _ ->
            dao.refreshHits(result.hits, hits)
        }
    }

    /**
     * get the saved hit,
     * @param id of the hit
     * @return flow of one hit
     */
    fun getHit(id: String): Flow<CallResult<Hit>> = getDatabaseFlow {
        dao.loadHit(id)
    }

    /**
     * Helper method that executes and emits database and network calls as a single Flow
     * @param databaseQuery method containing database call invocation
     * @param networkCall method containing network call invocation
     * @param saveCallResult method responsible for persisting network results to database
     * @return Flow that will report data state
     */
    private fun <T, A> getFlow(
        databaseQuery: suspend () -> T?,
        networkCall: suspend () -> CallResult<A>,
        saveCallResult: suspend (T?, A, extra: Map<String, String>) -> Unit
    ): Flow<CallResult<T>> =
        flow<CallResult<T>> {
            coroutineScope {
                val responseCall = async(Dispatchers.IO) { networkCall() }
                val dataCall = async(Dispatchers.IO) { databaseQuery() }
                val data = dataCall.await()
                if (data != null && (data !is List<*> || data.isNotEmpty())) {
                    emit(CallResult.success(data))
                }
                val response = responseCall.await()
                if (response.isSuccess() && response.data != null) {
                    saveCallResult(data, response.data, response.extra)
                    val newData = databaseQuery()
                    if (newData != data) {
                        emit(CallResult.success(newData))
                    }
                } else if (response.isFail()) {
                    emit(CallResult.error(response.message, response.code))
                }
            }
        }.onStart { emit(CallResult.loading()) }

    /**
     * Helper method that executes and emits network calls as a single Flow
     * @param networkCall method containing network call invocation
     * @param saveCallResult optional method responsible for persisting network results to database
     * @return Flow that will report data state
     */
    private fun <T> getNetworkFlow(
        networkCall: suspend () -> CallResult<T>,
        saveCallResult: (suspend (T, extra: Map<String, String>) -> Unit)? = null
    ): Flow<CallResult<T>> =
        flow<CallResult<T>> {
            coroutineScope {
                val responseCall = async(Dispatchers.IO) { networkCall() }
                val response = responseCall.await()
                if (response.isSuccess() && response.data != null) {
                    emit(CallResult.success(response.data))
                    saveCallResult?.invoke(response.data, response.extra)
                } else if (response.isFail()) {
                    emit(CallResult.error(response.message, response.code))
                }
            }
        }.onStart { emit(CallResult.loading()) }

    /**
     * Helper method that executes and emits database call as a single Flow
     * @param databaseQuery method containing database call invocation
     * @return Flow that will report data state
     */
    private fun <T> getDatabaseFlow(
        databaseQuery: suspend () -> T?
    ): Flow<CallResult<T>> =
        flow<CallResult<T>> {
            coroutineScope {
                val dataCall = async(Dispatchers.IO) { databaseQuery() }
                val data = dataCall.await()
                if (data != null && (data !is List<*> || data.isNotEmpty())) {
                    emit(CallResult.success(data))
                } else {
                    emit(CallResult.error())
                }
            }
        }.onStart { emit(CallResult.loading()) }
}