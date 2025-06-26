package model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import network.YouTubeApiService
import pojo.ChannelStatistics

class YouTubeRepository(private val apiService: YouTubeApiService, private val db: AppDatabase) {

    suspend fun getChannelStats(channelId: String, apiKey: String): ChannelStatistics {
        return apiService.getChannelStats(channelId = channelId, apiKey = apiKey).items.first().statistics
    }

    suspend fun getVideoStats(apiKey: String): List<VideoStatsEntity> {
        return withContext(Dispatchers.IO) {
            val response = apiService.getVideosStats(apiKey = apiKey)
            response.items.map {
                VideoStatsEntity(
                    videoId = it.id,
                    title = it.snippet.title,
                    views = it.statistics.viewCount.toInt(),
                    watchHours = it.statistics.viewCount.toInt() / 60, // Approximate hours
                    subscriptions = 0
                )
            }
        }
    }

    suspend fun saveStatsToDatabase(stats: List<VideoStatsEntity>) {
        withContext(Dispatchers.IO) {
            db.videoStatsDao().insertStats(stats)
        }
    }

    suspend fun getPreviousStats(): List<VideoStatsEntity> {
        return withContext(Dispatchers.IO) {
            db.videoStatsDao().getAllStats()
        }
    }
}