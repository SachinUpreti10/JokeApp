package com.jokesapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jokesapp.data.model.Joke

@Dao
interface JokeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertJoke(joke: Joke)

    @Query("SELECT * FROM joke_table")
    fun getJokes(): List<Joke>

    @Query("DELETE FROM joke_table where timestamp NOT IN (SELECT timestamp from joke_table ORDER BY timestamp DESC LIMIT 10)")
    suspend fun removeOldJokes()

}