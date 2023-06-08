package com.jokesapp.ui.main.adapters

import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jokesapp.R
import com.jokesapp.data.model.Joke
import com.jokesapp.databinding.ItemJokeBinding

class JokeAdapter(
    private val jokes: List<Joke>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        JokeHolder(ItemJokeBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = jokes.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is JokeHolder -> holder.bind()
        }
    }

    inner class JokeHolder(
        private val binding: ItemJokeBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.tvJoke.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(jokes[bindingAdapterPosition].joke, Html.FROM_HTML_MODE_LEGACY)
            } else {
                Html.fromHtml(jokes[bindingAdapterPosition].joke)
            }
            binding.tvJoke.setBackgroundResource(
                if (bindingAdapterPosition % 2 == 0) R.color.white
                else R.color.grey
            )
        }
    }
}