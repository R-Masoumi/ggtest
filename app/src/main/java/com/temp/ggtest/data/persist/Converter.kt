package com.temp.ggtest.data.persist

import androidx.room.TypeConverter
import java.time.LocalDateTime

/**
 * Converters for Room Database
 */
class Converter {
    @TypeConverter
    fun fromStringDate(value : String?): LocalDateTime? {
        if(value == null)
            return null
        val moshi = Db.moshi
        val adapter = moshi.adapter(LocalDateTime::class.java)
        return adapter.fromJson(value)
    }

    @TypeConverter
    fun toStringDate(list: LocalDateTime?): String?{
        if(list == null)
            return null
        val moshi = Db.moshi
        val adapter = moshi.adapter(LocalDateTime::class.java)
        return adapter.toJson(list)
    }
}
