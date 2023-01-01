package com.temp.ggtest.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Model Representing an author
 */
@JsonClass(generateAdapter = true)
data class Author(
    @Json(name = "id")
    val authorId: Long,
    val name: String?,
    val profile: String?
)