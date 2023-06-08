package com.jokesapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import java.util.Date

@Entity(tableName = "joke_table")
data class Joke(
    @PrimaryKey val timestamp: Long = Date().time,
    @Json(name = "joke")
    val joke: String = "",
)