package com.jokesapp.data.api

import kotlinx.coroutines.flow.flow

class ApiHelperImpl(
    private val apiService: ApiService
) : ApiHelper {

    override suspend fun getJoke() = flow {
        emit(apiService.getJoke())
    }

}