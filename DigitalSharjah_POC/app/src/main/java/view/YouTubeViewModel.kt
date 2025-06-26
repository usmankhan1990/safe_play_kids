package view

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import model.VideoStatsEntity
import model.YouTubeRepository

class YouTubeViewModel(private val repository: YouTubeRepository) : ViewModel() {

    private val _videoStats = MutableLiveData<List<VideoStatsEntity>>()
    val videoStats: LiveData<List<VideoStatsEntity>> get() = _videoStats

    fun fetchData(apiKey: String, channelId: String) {
        viewModelScope.launch {
            val currentStats = repository.getVideoStats(apiKey)
            val previousStats = repository.getPreviousStats()

            val comparedStats = currentStats.map { newStat ->
                val oldStat = previousStats.find { it.videoId == newStat.videoId }
                val viewDiff = if (oldStat != null) newStat.views - oldStat.views else 0

                newStat.copy(views = viewDiff)
            }

            _videoStats.postValue(comparedStats)
            repository.saveStatsToDatabase(currentStats)
        }
    }
}
