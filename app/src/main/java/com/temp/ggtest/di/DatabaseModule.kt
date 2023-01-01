package com.temp.ggtest.di

import android.content.Context
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.temp.ggtest.data.persist.HitDao
import com.temp.ggtest.data.persist.Db
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger-Hilt providers for Database components
 */
@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    /**
     * provides room database instance
     */
    @Provides
    @Singleton
    fun provideDb(@ApplicationContext context: Context, moshi: Moshi): Db {
        return Room.databaseBuilder(
            context,
            Db::class.java, "cache"
        ).fallbackToDestructiveMigration().build().apply { Db.moshi = moshi }
    }

    /**
     * provides room HitDao
     */
    @Provides
    @Singleton
    fun provideDao(db: Db): HitDao {
        return db.dao()
    }
}