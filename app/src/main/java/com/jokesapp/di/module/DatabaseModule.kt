package com.jokesapp.di.module

import androidx.room.Room
import com.jokesapp.data.database.DATABASE_NAME
import com.jokesapp.data.database.JokeDatabase
import org.koin.dsl.module

val databaseModule = module {
    single { Room.databaseBuilder(get(), JokeDatabase::class.java, DATABASE_NAME).build() }
    single { get<JokeDatabase>().getJokeDao() }
}