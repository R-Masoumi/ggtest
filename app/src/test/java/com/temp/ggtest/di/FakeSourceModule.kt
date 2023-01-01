package com.temp.ggtest.di

import com.temp.ggtest.util.Util.readFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import javax.inject.Named
import javax.inject.Singleton


@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [NetworkSourceModule::class]
)
class FakeSourceModule{
    @Provides
    @Singleton
    fun provideMockServer(): MockWebServer {
        val mockServer = MockWebServer()
        val c = this@FakeSourceModule
        mockServer.dispatcher = object : Dispatcher(){
            override fun dispatch(request: RecordedRequest): MockResponse = when (request.path) {
                "/search/applets?filter=author:geogebrateam" -> MockResponse().setResponseCode(200)
                    .setBody("sample.json".readFile(c))
                else -> MockResponse().setResponseCode(404)
            }
        }
        return mockServer
    }

    @Provides
    @Named("FlagNetworkExtensiveLog")
    fun provideLogFlag() = false

    @Provides
    @Singleton
    @Named("Url")
    fun provideUrl(mockServer : MockWebServer): HttpUrl = runBlocking{
        val url = async(Dispatchers.IO){ mockServer.url("/")}
        url.await()
    }
}