package com.safeplay.kids.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.safeplay.kids.network.YouTubeRepository
import com.safeplay.kids.pojo.SearchSuggestionItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: YouTubeRepository
) : ViewModel() {

    private val _suggestions = MutableLiveData<List<SearchSuggestionItem>>()
    val suggestions: LiveData<List<SearchSuggestionItem>> = _suggestions

    private val _videos = MutableLiveData<List<SearchSuggestionItem.VideoItem>>()
    val videos: LiveData<List<SearchSuggestionItem.VideoItem>> = _videos

    /** Fetch both: channel + kid-safe video results */
    fun searchAllKidsContent(query: String) {
        viewModelScope.launch {
            val channels = repo.searchChannels(query)
            val videos = repo.searchKidsVideos(query)

            val combined = buildList<SearchSuggestionItem> {
                addAll(channels)
                addAll(videos)
            }

            _suggestions.postValue(combined)
        }
    }

    /** Fetch all videos from a selected channel */
    fun loadVideos(channelId: String) {
        viewModelScope.launch {
            val vids = repo.getVideosFromChannel(channelId)
            _videos.postValue(vids)
        }
    }
}