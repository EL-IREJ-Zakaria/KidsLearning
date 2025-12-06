package com.example.kidslearning.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kidslearning.data.AppDatabase
import com.example.kidslearning.data.LetterProgress
import com.example.kidslearning.data.Letter
import com.example.kidslearning.data.LetterRepository
import kotlinx.coroutines.launch

/**
 * ViewModel pour gérer les lettres et la progression
 */
class LetterViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: LetterRepository
    
    private val _arabicLetters = MutableLiveData<List<Letter>>()
    val arabicLetters: LiveData<List<Letter>> = _arabicLetters

    private val _frenchLetters = MutableLiveData<List<Letter>>()
    val frenchLetters: LiveData<List<Letter>> = _frenchLetters

    private val _englishLetters = MutableLiveData<List<Letter>>()
    val englishLetters: LiveData<List<Letter>> = _englishLetters

    private val _currentLetter = MutableLiveData<Letter?>()
    val currentLetter: LiveData<Letter?> = _currentLetter
    
    init {
        val letterProgressDao = AppDatabase.getDatabase(application).letterProgressDao()
        repository = LetterRepository(application, letterProgressDao)
    }
    
    fun fetchArabicLetters() {
        viewModelScope.launch {
            _arabicLetters.value = repository.getLettersByLanguage("arabic")
        }
    }
    
    fun fetchFrenchLetters() {
        viewModelScope.launch {
            _frenchLetters.value = repository.getLettersByLanguage("french")
        }
    }

    fun fetchEnglishLetters() {
        viewModelScope.launch {
            _englishLetters.value = repository.getLettersByLanguage("english")
        }
    }

    fun fetchLetterById(letterId: String) {
        viewModelScope.launch {
            _currentLetter.value = repository.getLetterById(letterId)
        }
    }
    
    fun getProgress(letterId: String): LiveData<LetterProgress?> = repository.getLetterProgress(letterId)
    
    fun markAsTraced(letterId: String) {
        viewModelScope.launch {
            repository.insertOrUpdateProgress(LetterProgress(letterId = letterId, traced = true))
        }
    }
    
    fun updateLastPlayedSound(letterId: String) {
        viewModelScope.launch {
            repository.insertOrUpdateProgress(LetterProgress(letterId = letterId, lastPlayedSoundDate = System.currentTimeMillis()))
        }
    }
}