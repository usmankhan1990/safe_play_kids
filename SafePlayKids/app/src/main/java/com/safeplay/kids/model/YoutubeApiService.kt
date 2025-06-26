package com.safeplay.kids.pojo   // <- keep the same base package

import retrofit2.http.GET
import retrofit2.http.Query

interface YouTubeApiService {

    /*━━━━━━━━ search (channels) ━━━━━━━━*/
    @GET("search")
    suspend fun searchChannels(
        @Query("part")       part  : String = "snippet",
        @Query("q")          query : String,
        @Query("type")       type  : String = "channel",
        @Query("maxResults") max   : Int    = 10,
        @Query("safeSearch") safe  : String = "strict",
        @Query("key")        key   : String
    ): YouTubeSearchResponse

    /*━━━━━━━━ search (kids videos) ━━━━━*/
    @GET("search")
    suspend fun searchVideosByTopic(
        @Query("part")       part  : String = "snippet",
        @Query("q")          query : String,
        @Query("type")       type  : String = "video",
        @Query("topicId")    topic : String = "/m/0bzvm2",
        @Query("safeSearch") safe  : String = "strict",
        @Query("maxResults") max   : Int    = 10,
        @Query("key")        key   : String
    ): YouTubeSearchResponse

    /*━━━━━━━━ channel → uploads playlist id ━━━*/
    @GET("channels")
    suspend fun getUploadsPlaylist(
        @Query("part") part : String = "contentDetails",
        @Query("id")   id   : String,
        @Query("key")  key  : String
    ): ChannelResponse

    /*━━━━━━━━ playlistItems ━━━━━━━━━━━━━━━━━━━*/
    @GET("playlistItems")
    suspend fun getPlaylistVideos(
        @Query("part")       part     : String = "snippet,contentDetails",
        @Query("playlistId") playlist : String,
        @Query("maxResults") max      : Int    = 50,
        @Query("key")        key      : String
    ): PlaylistItemsResponse
}
