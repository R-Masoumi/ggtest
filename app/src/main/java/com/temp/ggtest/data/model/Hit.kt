package com.temp.ggtest.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import com.temp.ggtest.util.FixUrl
import com.temp.ggtest.util.ForceBoolean
import java.time.LocalDateTime

/**
 * Model Representing hits
 */
@JsonClass(generateAdapter = true)
data class Hits(
    val hits: List<Hit>,
)

/**
 * Entity Model Representing a hit
 */
@Entity
@JsonClass(generateAdapter = true)
data class Hit(
    @PrimaryKey
    val id: String,
    val title: String?,
    val type: String?,
    val dateCreated: LocalDateTime?,
    val dateModified: LocalDateTime?,
    @ForceBoolean
    val visibility: Boolean?,
    @FixUrl
    val thumbUrl: String?,
    @Embedded
    val creator: Author
)