package com.temp.ggtest.di

import android.content.Context
import coil.ImageLoader
import coil.memory.MemoryCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CoreModule {
    /**
     * provides configured ImageLoader to load images using coil
     */
    @Provides
    @Singleton
    fun provideImageLoader(@ApplicationContext context : Context) = ImageLoader.Builder(context)
        .memoryCache { MemoryCache.Builder(context).maxSizePercent(0.25).build() }
        .crossfade(true)
        .build()
}