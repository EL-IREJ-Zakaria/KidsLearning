package com.example.kidslearning.ui.lettertrace

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.kidslearning.data.Letter
import com.example.kidslearning.data.LetterProgress
import com.example.kidslearning.data.LetterRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for the LetterTraceActivity. It handles loading a specific letter,
 * managing its tracing progress, and updating sound playback status.
 */
class LetterTraceViewModel(application: Application, private val repository: LetterRepository, private val letterId: String) : AndroidViewModel(application) {

    private val _letter = MutableLiveData<Letter?>()
    val letter: LiveData<Letter?> = _letter

    // LiveData for tracking letter tracing progress
    val letterProgress: LiveData<LetterProgress?> = repository.getLetterProgress(letterId)

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    init {
        loadLetter()
    }

    /**
     * Loads the specific letter data from the repository based on the letterId.
     */
    private fun loadLetter() {
        _isLoading.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                _letter.value = repository.getLetterById(letterId)
            } catch (e: Exception) {
                _error.value = "Failed to load letter: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Updates the tracing status for the current letter.
     * @param traced True if the letter has been traced, false otherwise.
     */
    fun updateTracingStatus(traced: Boolean) {
        viewModelScope.launch {
            val currentProgress = letterProgress.value ?: LetterProgress(letterId = letterId)
            repository.insertOrUpdateProgress(currentProgress.copy(traced = traced))
        }
    }

    /**
     * Updates the last played sound date for the current letter.
     * This is called when the sound is played successfully.
     */
    fun updateLastPlayedSoundDate() {
        viewModelScope.launch {
            val currentProgress = letterProgress.value ?: LetterProgress(letterId = letterId)
            repository.insertOrUpdateProgress(currentProgress.copy(lastPlayedSoundDate = System.currentTimeMillis()))
        }
    }
}

/**
 * Factory for creating LetterTraceViewModel with a constructor that takes an Application,
 * LetterRepository, and letterId.
 */
class LetterTraceViewModelFactory(private val application: Application, private val repository: LetterRepository, private val letterId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LetterTraceViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LetterTraceViewModel(application, repository, letterId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
