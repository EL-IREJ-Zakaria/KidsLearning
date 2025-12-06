package com.example.kidslearning

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.navigation.navArgs
import com.example.kidslearning.databinding.ActivityLetterTraceBinding
import com.example.kidslearning.ui.tracing.TracingCanvasView
import com.example.kidslearning.viewmodel.LetterViewModel

class LetterTraceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLetterTraceBinding
    private val letterViewModel: LetterViewModel by viewModels() // Initialize ViewModel
    private val args: LetterTraceActivityArgs by navArgs()
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLetterTraceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val letterId = args.letterId

        // Observe the letter data
        letterViewModel.currentLetter.observe(this, Observer {
            letter ->
            letter?.let {
                binding.letterDisplayTextView.text = it.character
                playLetterSound(it.soundFileName)
                binding.tracingCanvasView.setLetterTracingPath(it.tracingPathData)
            }
        })
        letterViewModel.fetchLetterById(letterId)

        binding.replaySoundButton.setOnClickListener {
            playLetterSound(letterViewModel.currentLetter.value?.soundFileName)
        }

        binding.clearDrawingButton.setOnClickListener {
            binding.tracingCanvasView.clearDrawing()
        }

        binding.backButton.setOnClickListener {
            onBackPressed() // Navigate back
        }
    }

    private fun playLetterSound(soundFileName: String?) {
        soundFileName?.let {
            val resourceId = resources.getIdentifier(it.removeSuffix(".mp3"), "raw", packageName)
            if (resourceId != 0) {
                mediaPlayer?.release()
                mediaPlayer = MediaPlayer.create(this, resourceId)
                mediaPlayer?.start()
            } else {
                // Handle case where sound file is not found
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
