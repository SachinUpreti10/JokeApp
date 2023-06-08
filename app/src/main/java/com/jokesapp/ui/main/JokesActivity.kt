package com.jokesapp.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.jokesapp.R
import com.jokesapp.data.model.Joke
import com.jokesapp.databinding.ActivityJokesBinding
import com.jokesapp.ui.main.adapters.JokeAdapter
import com.jokesapp.ui.main.viewmodel.JokeViewModel
import com.jokesapp.utils.Status
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class JokesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJokesBinding
    private val jokeViewModel: JokeViewModel by viewModel()

    private lateinit var jokeAdapter: JokeAdapter
    private val jokeList = ArrayList<Joke>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJokesBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        setObserver()
        setAdapter()
    }

    private fun setObserver() {
        lifecycleScope.launch {
            jokeViewModel.timerStateFlow.collectLatest { sec ->
                binding.tvTimer.text = buildString {
                    append(getString(R.string.next_joke_fetch_in))
                    append(": 00:")
                    append(if (sec < 10) "0$sec" else sec)
                }
            }
        }
        jokeViewModel.jokesLiveData.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { list ->
                        jokeList.addAll(list)
                        if (jokeList.size >= 10) {
                            jokeList.subList(0, jokeList.size - 10).clear()
                        }
                        binding.progressBar.isVisible = false
                        jokeAdapter.notifyDataSetChanged()
                    }
                }

                Status.LOADING -> {
                    binding.progressBar.isVisible = true
                }

                Status.ERROR -> {
                    binding.progressBar.isVisible = false
                    Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setAdapter() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        jokeAdapter = JokeAdapter(jokeList)
        binding.recyclerView.adapter = jokeAdapter
    }
}