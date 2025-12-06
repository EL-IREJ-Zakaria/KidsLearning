package com.example.kidslearning.ui.alphabetlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.kidslearning.data.Letter
import com.example.kidslearning.data.LetterRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for the alphabet list fragments (ArabicLettersFragment and FrenchLettersFragment).
 * It fetches and exposes the list of letters for a given language.
 */
class AlphabetViewModel(private val repository: LetterRepository) : ViewModel() {

    private val _letters = MutableLiveData<List<Letter>>()
    val letters: LiveData<List<Letter>> = _letters

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    /**
     * Fetches the list of letters for the specified language.
     * @param language The language to fetch letters for (e.g., "arabic", "french").
     */
    fun fetchLetters(language: String) {
        _isLoading.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                val result = repository.getLettersByLanguage(language)
                _letters.value = result
            } catch (e: Exception) {
                _error.value = "Failed to load letters: ${e.localizedMessage}"
                _letters.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}


