package com.example.kidslearning.utils

import android.content.Context
import android.media.MediaPlayer
import androidx.annotation.RawRes

/**
 * Gestionnaire de lecture des sons
 */
class SoundPlayer(private val context: Context) {
    
    private var mediaPlayer: MediaPlayer? = null
    
    fun playSound(@RawRes soundResId: Int) {
        release()
        try {
            mediaPlayer = MediaPlayer.create(context, soundResId)
            mediaPlayer?.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    fun playSoundByName(soundFileName: String) {
        val resId = context.resources.getIdentifier(
            soundFileName.replace(".mp3", ""),
            "raw",
            context.packageName
        )
        if (resId != 0) {
            playSound(resId)
        }
    }
    
    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
