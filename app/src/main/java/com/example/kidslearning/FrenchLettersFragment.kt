package com.example.kidslearning

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.kidslearning.data.Letter
import com.example.kidslearning.databinding.FragmentFrenchLettersBinding
import com.example.kidslearning.ui.alphabetlist.LetterAdapter
import com.example.kidslearning.viewmodel.LetterViewModel

/**
 * A Fragment to display the list of French letters using a RecyclerView.
 * It interacts with the LetterViewModel to fetch and display letter data.
 */
class FrenchLettersFragment : Fragment() {

    private var _binding: FragmentFrenchLettersBinding? = null
    private val binding get() = _binding!!

    private val letterViewModel: LetterViewModel by viewModels()

    private lateinit var letterAdapter: LetterAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFrenchLettersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()

        // Fetch French letters
        letterViewModel.fetchFrenchLetters()
    }

    /**
     * Sets up the RecyclerView with its adapter and click listener.
     */
    private fun setupRecyclerView() {
        letterAdapter = LetterAdapter { letter ->
            // Navigate to LetterTraceActivity with the letter ID
            val action = FrenchLettersFragmentDirections.actionFrenchLettersFragmentToLetterTraceActivity(letter.id)
            findNavController().navigate(action)
        }
        binding.frenchLettersRecyclerView.adapter = letterAdapter
    }

    /**
     * Observes changes in the ViewModel's LiveData and updates the UI accordingly.
     */
    private fun observeViewModel() {
        letterViewModel.frenchLetters.observe(viewLifecycleOwner) { letters ->
            letterAdapter.submitList(letters)
        }

        // Removed isLoading and error observations as LetterViewModel doesn't directly manage them for letter lists now.
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
