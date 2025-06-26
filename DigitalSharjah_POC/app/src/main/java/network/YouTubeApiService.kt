package network

import pojo.YouTubeChannelResponse
import pojo.YouTubeVideoResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface YouTubeApiService {
    @GET("youtube/v3/channels")
    suspend fun getChannelStats(
        @Query("part") part: String = "statistics",
        @Query("id") channelId: String,
        @Query("key") apiKey: String
    ): YouTubeChannelResponse

    @GET("youtube/v3/videos")
    suspend fun getVideosStats(
        @Query("part") part: String = "statistics,snippet",
        @Query("chart") chart: String = "mostPopular",
        @Query("maxResults") maxResults: Int = 10,
        @Query("key") apiKey: String
    ): YouTubeVideoResponse
}
