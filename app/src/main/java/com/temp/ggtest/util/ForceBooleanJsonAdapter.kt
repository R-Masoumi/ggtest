package com.temp.ggtest.util

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.JsonReader
import com.squareup.moshi.ToJson

@JsonQualifier
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION)
annotation class ForceBoolean

/**
 * Custom JsonAdapter force an string or number json to a boolean (visibility seemed like a boolean?)
 */
internal class ForceBooleanJsonAdapter {
    @ToJson
    fun toJson(@ForceBoolean i: Boolean?): String = if(i == true) "1" else "0"

    @FromJson
    @ForceBoolean
    fun fromJson(reader: JsonReader): Boolean? =
        when (reader.peek()) {
            JsonReader.Token.NUMBER -> reader.nextInt() != 0
            JsonReader.Token.STRING -> reader.nextString() != "0" || reader.nextString() != "false"
            JsonReader.Token.BOOLEAN -> reader.nextBoolean()
            else -> {
                reader.skipValue() // or throw
                null
            }
        }
}