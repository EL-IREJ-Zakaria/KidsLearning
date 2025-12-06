package com.example.kidslearning.data

import android.content.Context
import androidx.lifecycle.LiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

/**
 * Repository class for managing letter data and letter progress.
 * It abstracts the data sources (JSON assets for letters, Room database for progress).
 */
class LetterRepository(private val context: Context, private val letterProgressDao: LetterProgressDao) {

    private var allLetters: List<Letter>? = null

    /**
     * Loads letter data from the 'assets/letters.json' file.
     * This operation is suspended to be called from a coroutine.
     * @return A list of Letter objects.
     */
    private suspend fun loadLettersFromAssets(): List<Letter> {
        if (allLetters == null) {
            allLetters = withContext(Dispatchers.IO) {
                try {
                    val jsonString: String
                    context.assets.open("letters.json").bufferedReader().use { 
                        jsonString = it.readText() 
                    }
                    val listType = object : TypeToken<List<Letter>>() {}.type
                    Gson().fromJson<List<Letter>>(jsonString, listType)
                } catch (ioException: IOException) {
                    ioException.printStackTrace()
                    emptyList()
                }
            }
        }
        return allLetters ?: emptyList()
    }

    /**
     * Retrieves all letters for a specific language.
     * @param language The language of the letters (e.g., "arabic", "french").
     * @return A list of Letter objects for the specified language.
     */
    suspend fun getLettersByLanguage(language: String): List<Letter> {
        val letters = loadLettersFromAssets()
        return letters.filter { it.language == language }
    }

    /**
     * Retrieves a single letter by its ID.
     * @param letterId The unique ID of the letter.
     * @return The Letter object, or null if not found.
     */
    suspend fun getLetterById(letterId: String): Letter? {
        val letters = loadLettersFromAssets()
        return letters.find { it.id == letterId }
    }

    /**
     * Inserts or updates the progress for a given letter.
     * @param letterProgress The LetterProgress object to be saved.
     */
    suspend fun insertOrUpdateProgress(letterProgress: LetterProgress) {
        letterProgressDao.insertOrUpdateProgress(letterProgress)
    }

    /**
     * Retrieves the LiveData progress for a specific letter.
     * @param letterId The unique ID of the letter.
     * @return LiveData containing the LetterProgress for the specified letter.
     */
    fun getLetterProgress(letterId: String): LiveData<LetterProgress?> {
        return letterProgressDao.getProgress(letterId)
    }
}
