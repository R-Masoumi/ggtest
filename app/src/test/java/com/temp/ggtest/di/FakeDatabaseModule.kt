package com.temp.ggtest.di

import android.content.Context
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.temp.ggtest.data.persist.Db
import com.temp.ggtest.data.persist.HitDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
class FakeDatabaseModule {
    @Provides
    @Singleton
    fun provideDb(@ApplicationContext context: Context, moshi: Moshi): Db {
        return Room.inMemoryDatabaseBuilder(context, Db::class.java)
            .allowMainThreadQueries().build().apply { Db.moshi = moshi }
    }

    @Provides
    @Singleton
    fun provideDao(db : Db): HitDao {
        return db.dao()
    }
}