package com.example.kidslearning.ui.alphabetlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kidslearning.data.Letter
import com.example.kidslearning.databinding.ItemLetterBinding

/**
 * RecyclerView adapter for displaying a list of letters. It uses DiffUtil for efficient updates.
 */
class LetterAdapter(private val onClick: (Letter) -> Unit) : ListAdapter<Letter, LetterAdapter.LetterViewHolder>(LetterDiffCallback()) {

    /**
     * ViewHolder for a single letter item in the RecyclerView.
     */
    class LetterViewHolder(private val binding: ItemLetterBinding, private val onClick: (Letter) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(letter: Letter) {
            binding.letterButton.text = letter.character
            binding.letterButton.setOnClickListener { onClick(letter) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LetterViewHolder {
        val binding = ItemLetterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LetterViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: LetterViewHolder, position: Int) {
        val letter = getItem(position)
        holder.bind(letter)
    }
}

/**
 * DiffUtil.ItemCallback for calculating the differences between two lists of Letters.
 */
class LetterDiffCallback : DiffUtil.ItemCallback<Letter>() {
    override fun areItemsTheSame(oldItem: Letter, newItem: Letter): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Letter, newItem: Letter): Boolean {
        return oldItem == newItem
    }
}
