package com.jokesapp.di.module

import com.jokesapp.ui.main.viewmodel.JokeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        JokeViewModel(get(), get())
    }
}
