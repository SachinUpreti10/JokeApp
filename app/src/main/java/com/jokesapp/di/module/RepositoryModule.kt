package com.jokesapp.di.module

import com.jokesapp.ui.main.repository.JokeRepository
import org.koin.dsl.module

val repoModule = module {
    single {
        JokeRepository(get(), get())
    }
}