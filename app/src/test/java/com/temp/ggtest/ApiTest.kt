package com.temp.ggtest

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.squareup.moshi.Moshi
import com.temp.ggtest.data.model.Hits
import com.temp.ggtest.data.net.Api
import com.temp.ggtest.di.CoreModule
import com.temp.ggtest.di.DatabaseModule
import com.temp.ggtest.di.NetworkSourceModule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
@HiltAndroidTest
@Config(application = HiltTestApplication::class)
class ApiTest {
    @Inject
    lateinit var mockServer: MockWebServer
    @Inject
    lateinit var api : Api
    @Inject
    lateinit var moshi: Moshi

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun testHits() {
        testRequest("sample.json", Hits::class.java) {api.getHits()}
    }

    private fun <T : Any> testRequest(fileName : String, adapterType : Class<T>, request : suspend()-> Response<T>){
        val adapter = moshi.adapter(adapterType)
        val input = javaClass.classLoader!!.getResource(fileName).readText()
        val json = adapter.fromJson(input)!!
        runBlocking {
            val response = request().body()
            Assert.assertEquals(json, response)
        }
    }
}