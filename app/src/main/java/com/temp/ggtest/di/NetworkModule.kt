package com.temp.ggtest.di

import com.squareup.moshi.Moshi
import com.temp.ggtest.data.net.Api
import com.temp.ggtest.util.FixUrlJsonAdapter
import com.temp.ggtest.util.ForceBooleanJsonAdapter
import com.temp.ggtest.util.LocalDateTimeAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.time.LocalDateTime
import javax.inject.Named
import javax.inject.Singleton


/**
 * Dagger-Hilt providers for network components
 */
@Module
@InstallIn(SingletonComponent::class)
class NetworkModule{

    /**
     * provides LoggingInterceptor to set logging config in okHttp interface
     */
    @Provides
    @Singleton
    fun provideLoggingInterceptor(@Named("FlagNetworkExtensiveLog") extensiveLog : Boolean): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = if (extensiveLog)
            HttpLoggingInterceptor.Level.BODY
        else
            HttpLoggingInterceptor.Level.NONE
        return interceptor
    }

    /**
     * provides okHttp instance
     */
    @Provides
    @Singleton
    fun provideOkHttp(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
    }

    /**
     * provides Moshi instance configured with Json converters
     */
    @Provides
    @Singleton
    fun provideMoshi() : Moshi = Moshi.Builder()
        .add(LocalDateTime::class.java, LocalDateTimeAdapter().nullSafe())
        .add(ForceBooleanJsonAdapter()).add(FixUrlJsonAdapter()).build()

    /**
     * provides retrofit instance
     */
    @Provides
    @Singleton
    fun provideRetrofit(httpClient: OkHttpClient,@Named("Url") url : HttpUrl, moshi : Moshi): Retrofit {
        val retroBuilder = Retrofit.Builder()
            .client(httpClient)
            .baseUrl(url)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
        return retroBuilder.build()
    }

    /**
     * provides retrofit Api for network calls
     */
    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): Api {
        return retrofit.create(Api::class.java)
    }
}