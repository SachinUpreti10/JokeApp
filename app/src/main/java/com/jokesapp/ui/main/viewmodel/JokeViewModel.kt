package com.jokesapp.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jokesapp.data.model.Joke
import com.jokesapp.ui.main.repository.JokeRepository
import com.jokesapp.utils.NetworkHelper
import com.jokesapp.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class JokeViewModel(
    private val jokeRepository: JokeRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    private var job: Job? = null
    private val _jokesLiveData = MutableLiveData<Resource<List<Joke>>>()
    val jokesLiveData: LiveData<Resource<List<Joke>>>
        get() = _jokesLiveData
    private val _timerStateFlow = MutableStateFlow<Int>(0)
    val timerStateFlow: MutableStateFlow<Int>
        get() = _timerStateFlow

    init {
        fetchJoke()
    }

    private fun fetchJoke() {
        _jokesLiveData.value = Resource.loading(null)
        var jokeList: List<Joke>? = null
        CoroutineScope(Dispatchers.IO).launch {
            try {
                jokeList = jokeRepository.getJokeFromDatabase()
            } catch (e: Exception) {
                _jokesLiveData.value = Resource.error("Error in fetching jokes from database", null)
            } finally {
                withContext(Dispatchers.Main) {
                    _jokesLiveData.value = Resource.success(jokeList)
                    fetchJokeFromApi()
                }
            }
        }
    }

    private fun fetchJokeFromApi() {
        viewModelScope.launch {
            if (networkHelper.isNetworkConnected()) {
                jokeRepository.getJokeFromApi()
                    .catch { e ->
                        _jokesLiveData.value = Resource.error(e.message.toString(), null)
                    }.collect {
                        initTimer()
                        if (it.isSuccessful) {
                            if (it.body() == null) {
                                _jokesLiveData.value =
                                    Resource.error("Error in fetching joke from Api", null)
                            } else {
                                withContext(Dispatchers.IO) {
                                    jokeRepository.insertJoke(it.body()!!)
                                    jokeRepository.removeOldJokes()
                                }
                                _jokesLiveData.value = Resource.success(listOf(it.body()!!))
                            }
                        } else _jokesLiveData.value =
                            Resource.error(it.errorBody().toString(), null)
                    }
            } else {
                _jokesLiveData.value = Resource.error("No internet connection", null)
            }
        }
    }

    private fun initTimer(totalSeconds: Int = 60) {
        if (job != null) {
            job?.cancel()
            job = null
        }
        job = viewModelScope.launch {
            flow {
                for (sec in totalSeconds - 1 downTo 0) {
                    emit(sec)
                    delay(1000)
                }
            }.onCompletion {
                fetchJokeFromApi()
            }.collect { sec ->
                _timerStateFlow.value = sec
            }
        }
    }
}