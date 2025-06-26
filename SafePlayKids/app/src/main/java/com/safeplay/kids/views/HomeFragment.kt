package com.safeplay.kids.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels   // <-- this import MUST exist (and Fragment-ktx on classpath)
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.safeplay.kids.R
import com.safeplay.kids.adapter.ChannelSuggestionAdapter
import com.safeplay.kids.adapter.SearchSuggestionAdapter
import com.safeplay.kids.adapter.VideoAdapter
import com.safeplay.kids.databinding.FragmentHomeBinding
import com.safeplay.kids.pojo.SearchSuggestionItem
import com.safeplay.kids.viewModel.HomeViewModel
import com.safeplay.kids.views.VideoPlayerFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// file: ui/HomeFragment.kt
@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    private lateinit var searchAdapter: SearchSuggestionAdapter

    private val videoAdapter = VideoAdapter { videoId ->
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, VideoPlayerFragment.newInstance(videoId.videoId))
            .addToBackStack(null)
            .commit()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Setup RecyclerView for videos
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = videoAdapter

        // Setup adapter for search suggestions
        searchAdapter = SearchSuggestionAdapter(requireContext()) { item ->
            when (item) {
                is SearchSuggestionItem.ChannelItem -> {
                    Log.d("SuggestionClick", "Clicked channel: ${item.title}")
                    binding.edtSearch.setText(item.title)
                    viewModel.loadVideos(item.id) // Correct usage: item.id (String)
                }
                is SearchSuggestionItem.VideoItem -> {
                    Log.d("SuggestionClick", "Clicked video: ${item.videoId}")
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, VideoPlayerFragment.newInstance(item.videoId)) // Correct usage
                        .addToBackStack(null)
                        .commit()
                }
            }
        }

        binding.edtSearch.setAdapter(searchAdapter)

        // Search when user types at least 3 characters
        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = Unit
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if ((s?.length ?: 0) >= 3) {
                    Log.d("SearchWatcher", "Calling API with: $s")
                    viewModel.searchAllKidsContent(s.toString())
                }
            }
        })

        // Observe channel + video suggestions
        viewModel.suggestions.observe(viewLifecycleOwner) {
            Log.d("Observer", "Got ${it.size} suggestions")
            searchAdapter.setItems(it)
        }

        // Observe selected channel’s video list
        viewModel.videos.observe(viewLifecycleOwner) {
            Log.d("Observer", "Got ${it.size} videos")
            videoAdapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}