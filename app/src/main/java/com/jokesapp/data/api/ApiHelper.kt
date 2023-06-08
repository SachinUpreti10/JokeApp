package com.jokesapp.data.api

import com.jokesapp.data.model.Joke
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface ApiHelper {
    suspend fun getJoke(): Flow<Response<Joke>>
}