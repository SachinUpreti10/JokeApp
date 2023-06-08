package com.jokesapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jokesapp.data.model.Joke

const val DATABASE_NAME = "joke-database"

@Database(entities = [Joke::class], version = 1)
abstract class JokeDatabase : RoomDatabase() {
    abstract fun getJokeDao(): JokeDao
}