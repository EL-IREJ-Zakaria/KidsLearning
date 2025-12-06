package com.example.kidslearning.utils

import android.content.Context
import com.google.gson.Gson
import com.example.kidslearning.data.Letter
import com.google.gson.reflect.TypeToken

/**
 * Utilitaire pour charger les lettres depuis le fichier JSON
 */
object JsonLoader {
    
    fun loadLetters(context: Context): List<Letter> {
        return try {
            val jsonString = context.assets.open("letters.json").bufferedReader().use { it.readText() }
            val type = object : TypeToken<List<Letter>>() {}.type
            Gson().fromJson(jsonString, type)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
    
    fun loadArabicLetters(context: Context): List<Letter> {
        return loadLetters(context).filter { it.language == "arabic" }
    }
    
    fun loadFrenchLetters(context: Context): List<Letter> {
        return loadLetters(context).filter { it.language == "french" }
    }
    
    fun loadEnglishLetters(context: Context): List<Letter> {
        return loadLetters(context).filter { it.language == "english" }
    }
}
