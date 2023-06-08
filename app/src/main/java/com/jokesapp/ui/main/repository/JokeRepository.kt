package com.jokesapp.ui.main.repository

import com.jokesapp.data.api.ApiHelper
import com.jokesapp.data.database.JokeDao
import com.jokesapp.data.model.Joke

class JokeRepository(
    private val jokeDao: JokeDao,
    private val apiHelper: ApiHelper
) {

    suspend fun getJokeFromApi() = apiHelper.getJoke()

    fun getJokeFromDatabase() = jokeDao.getJokes()

    suspend fun insertJoke(joke: Joke) = jokeDao.insertJoke(joke)

    suspend fun removeOldJokes() = jokeDao.removeOldJokes()


}