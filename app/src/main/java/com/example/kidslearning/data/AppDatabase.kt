package com.example.kidslearning.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * The Room database for the Kids Learning app. It manages the LetterProgress entity.
 * This abstract class serves as the main access point for the underlying connection to your app's relational data.
 */
@Database(entities = [LetterProgress::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Provides the Data Access Object (DAO) for LetterProgress.
     * @return The LetterProgressDao interface.
     */
    abstract fun letterProgressDao(): LetterProgressDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Retrieves the singleton instance of the AppDatabase.
         * If the instance is null, it creates a new database instance.
         *
         * @param context The application context.
         * @return The singleton instance of AppDatabase.
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "kids_learning_db"
                )
                .fallbackToDestructiveMigration() // Simple migration strategy for development
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
