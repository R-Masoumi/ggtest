package com.temp.ggtest.util

import com.squareup.moshi.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * Custom JsonAdapter to parse the date provided in a LocalDateTime
 */
class LocalDateTimeAdapter : JsonAdapter<LocalDateTime>() {
    companion object {
        private val FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    }

    override fun toJson(writer: JsonWriter, value: LocalDateTime?) {
        if (value == null) {
            writer.nullValue()
        } else {
            val string = FORMATTER.format(value)
            writer.value(string)
        }
    }

    override fun fromJson(reader: JsonReader): LocalDateTime? {
        if (reader.peek() == JsonReader.Token.NULL) {
            return reader.nextNull<LocalDateTime>()
        }
        val string = reader.nextString()
        val temp = try {
            FORMATTER.parse(string)
        }catch (e : DateTimeParseException){
            reader.nextNull<LocalDateTime>()
        }
        return LocalDateTime.from(temp)
    }
}