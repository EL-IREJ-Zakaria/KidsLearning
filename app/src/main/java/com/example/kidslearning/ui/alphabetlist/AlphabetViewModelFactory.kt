package com.example.kidslearning.ui.alphabetlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kidslearning.data.LetterRepository

/**
 * Factory for creating AlphabetViewModel with repository dependency.
 */
class AlphabetViewModelFactory(private val repository: LetterRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlphabetViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AlphabetViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
