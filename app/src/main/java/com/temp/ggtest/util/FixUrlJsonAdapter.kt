package com.temp.ggtest.util

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.JsonReader
import com.squareup.moshi.ToJson

@JsonQualifier
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION)
annotation class FixUrl

/**
 * Custom JsonAdapter to fix malformed image urls (some images had $1 in their name which did not lead to an image)
 */
internal class FixUrlJsonAdapter {
    @ToJson
    fun toJson(@FixUrl str: String?): String = str.orEmpty()

    @FromJson
    @FixUrl
    fun fromJson(reader: JsonReader): String? =
        when (reader.peek()) {
            JsonReader.Token.STRING -> reader.nextString()
                .replace("$1", "")
            else -> {
                reader.skipValue() // or throw
                null
            }
        }
}