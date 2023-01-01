package com.temp.ggtest.data.net

import com.temp.ggtest.data.CallResult
import com.temp.ggtest.data.model.Hits
import okhttp3.internal.toHeaderList
import retrofit2.Response
import retrofit2.http.*
import java.lang.Exception

/**
 * Retrofit Api interface
 */
interface Api{
    /**
     * get list of hits
     * @return response of list of hits
     */
    @GET("search/applets?filter=author:geogebrateam")
    suspend fun getHits() : Response<Hits>

    companion object {
        /**
         * Helper method which can be used to safely do an API call from the interface
         * @param call the actual suspending network call
         * @return CallResult instance with transformed header and data
         */
        suspend fun <T : Any> apiCall(call: suspend () -> Response<T>): CallResult<T> {
            try{
                val response = call.invoke()
                val body = response.body()
                if (response.isSuccessful) {
                    val headers = response.headers().toHeaderList().associate { it.name.utf8() to it.value.utf8() }
                    return CallResult.success(body, headers,"",response.code())
                }
                return CallResult.error(response.message(), response.code(),null)
            }catch (e : Exception){
                return CallResult.error(e.message ?: e.toString())
            }
        }
    }
}