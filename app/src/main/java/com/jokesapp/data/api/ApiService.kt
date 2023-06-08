package com.jokesapp.data.api

import com.jokesapp.data.model.Joke
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("api?format=json")
    suspend fun getJoke(): Response<Joke>

}