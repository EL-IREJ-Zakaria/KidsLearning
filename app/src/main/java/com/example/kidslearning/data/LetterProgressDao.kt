package com.example.kidslearning.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Data Access Object (DAO) for the LetterProgress entity.
 * This interface defines the database interactions, such as querying and inserting progress data.
 */
@Dao
interface LetterProgressDao {

    /**
     * Inserts or updates a letter's progress in the database.
     * If a record with the same primary key already exists, it will be replaced.
     *
     * @param letterProgress The LetterProgress object to be inserted or updated.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProgress(letterProgress: LetterProgress)

    /**
     * Retrieves the progress for a specific letter by its ID.
     *
     * @param letterId The unique ID of the letter.
     * @return A LiveData object holding the LetterProgress, which can be null if no progress exists.
     */
    @Query("SELECT * FROM letter_progress WHERE letterId = :letterId")
    fun getProgress(letterId: String): LiveData<LetterProgress?>

    /**
     * Retrieves all letter progress records from the database.
     *
     * @return A LiveData list of all LetterProgress objects.
     */
    @Query("SELECT * FROM letter_progress")
    fun getAllProgress(): LiveData<List<LetterProgress>>
}
