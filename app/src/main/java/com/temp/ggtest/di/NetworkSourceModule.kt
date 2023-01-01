package com.temp.ggtest.di

import android.content.Context
import com.temp.ggtest.BuildConfig
import com.temp.ggtest.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import javax.inject.Named
import javax.inject.Singleton

/**
 * Dagger-Hilt providers for network source components.
 * this component can be swapped for debug environments
 * to provide a mock server to network components instead
 */
@Module
@InstallIn(SingletonComponent::class)
class NetworkSourceModule {
    /**
     * provides base Url for retrofit
     */
    @Provides
    @Singleton
    @Named("Url")
    fun provideUrl(@ApplicationContext context: Context): HttpUrl {
        return context.getString(R.string.SERVER_URL).toHttpUrl()
    }

    /**
     * provides logging flag to turn extensive logging on/off
     */
    @Provides
    @Named("FlagNetworkExtensiveLog")
    fun provideLogFlag() = BuildConfig.DEBUG
}