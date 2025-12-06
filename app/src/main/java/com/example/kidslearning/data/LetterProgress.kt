package com.example.kidslearning.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "letter_progress")
data class LetterProgress(
    @PrimaryKey
    val letterId: String, // Corresponds to Letter.id
    val traced: Boolean = false,
    val lastPlayedSoundDate: Long? = null // Timestamp
)
