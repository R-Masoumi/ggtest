package com.temp.ggtest

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.squareup.moshi.Moshi
import com.temp.ggtest.data.CallResult
import com.temp.ggtest.data.DataSource
import com.temp.ggtest.data.model.Hits
import com.temp.ggtest.data.persist.Db
import com.temp.ggtest.di.DatabaseModule
import com.temp.ggtest.di.NetworkSourceModule
import com.temp.ggtest.util.Util.readFile
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.IOException
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
@HiltAndroidTest
@Config(application = HiltTestApplication::class)
class DataSourceTest {
    @Inject
    lateinit var dataSource: DataSource
    @Inject
    lateinit var db: Db
    @Inject
    lateinit var moshi: Moshi

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun init() {
        hiltRule.inject()
    }

    @After
    @Throws(IOException::class)
    fun destroy() {
        db.close()
    }

    @Test
    fun testHits() {
        testRequest("sample.json", Hits::class.java, { it.hits }) { dataSource.getHits() }
    }

    private fun <T, A> testRequest(
        fileName: String, adapterType: Class<T>,
        converter: (T) -> A, request: () -> Flow<CallResult<A>>
    ) {
        val adapter = moshi.adapter(adapterType)
        val json = adapter.fromJson(fileName.readFile(this@DataSourceTest))!!
        val flow = request()
        runBlocking {
            flow.collect {
                Log.d("$adapterType source test", "$it")
                if (it.isSuccess()) {
                    Assert.assertEquals(converter(json), it.data)
                }
            }
        }
    }
}