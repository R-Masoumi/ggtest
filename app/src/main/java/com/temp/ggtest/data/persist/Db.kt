package com.temp.ggtest.data.persist

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.squareup.moshi.Moshi
import com.temp.ggtest.data.model.Hit

/**
 * Room database
 */
@Database(entities = [Hit::class], version = 1)
@TypeConverters(Converter::class)
abstract class Db : RoomDatabase(){
    companion object {
        lateinit var moshi: Moshi
    }
    abstract fun dao() : HitDao
}