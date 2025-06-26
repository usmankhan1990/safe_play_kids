package com.safeplay.kids.network

import android.util.Log
import com.safeplay.kids.pojo.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

/*──────────────── helper ────────────────*/
private suspend fun okhttp3.ResponseBody?.asString(): String =
    withContext(Dispatchers.IO) { this@asString?.string().orEmpty() }

@Singleton
class YouTubeRepository @Inject constructor(
    private val api: YouTubeApiService
) {
    /*  ❗ In production move this to gradle-prop + BuildConfig                */
    private val key = "AIzaSyD3h4aBreu0jWGWBBwA4L8e7q8q0GjAF_o"

    /*━━━━━━━━━━━━━━━━ 1) CHANNEL SUGGESTIONS ━━━━━━━━━━━━━━━*/
    suspend fun searchChannels(query: String): List<SearchSuggestionItem.ChannelItem> =
        withContext(Dispatchers.IO) {
            try {
                Log.d("YTRepo", "Sending searchChannels: $query")
                api.searchChannels(query = query, key = key)
                    .items.orEmpty().mapNotNull { it.toChannelSuggestion() }
            } catch (e: HttpException) {                 // ❶  body-aware catch
                val txt = e.response()?.errorBody().asString()
                Log.e("YTRepo", "searchChannels failed\n$txt", e)
                emptyList()
            } catch (e: Exception) {                     // ❷  fallback
                Log.e("YTRepo", "searchChannels failed (generic)", e)
                emptyList()
            }
        }

    /*━━━━━━━━━━━━━━━━ 2) KIDS VIDEO SUGGESTIONS ━━━━━━━━━━━━*/
    suspend fun searchKidsVideos(query: String): List<SearchSuggestionItem.VideoItem> =
        withContext(Dispatchers.IO) {
            try {
                Log.d("YTRepo", "Sending searchKidsVideos: $query")
                api.searchVideosByTopic(query = query, key = key)
                    .items.orEmpty()
                    .filterNot { it.snippet?.title?.contains("short", true) == true }
                    .mapNotNull { it.toVideoSuggestion() }
            } catch (e: HttpException) {
                val txt = e.response()?.errorBody().asString()
                Log.e("YTRepo", "searchKidsVideos failed\n$txt", e)
                emptyList()
            } catch (e: Exception) {
                Log.e("YTRepo", "searchKidsVideos failed (generic)", e)
                emptyList()
            }
        }


    /*━━━━━━━━━━━━━━━━ 3) ALL VIDEOS OF A CHANNEL ━━━━━━━━━━*/
    suspend fun getVideosFromChannel(channelId: String): List<SearchSuggestionItem.VideoItem> =
        withContext(Dispatchers.IO) {
            try {
                val uploads = api.getUploadsPlaylist(id = channelId, key = key)
                    .items?.firstOrNull()
                    ?.contentDetails?.relatedPlaylists?.uploads
                    ?: return@withContext emptyList()

                api.getPlaylistVideos(playlist = uploads, key = key)
                    .items.orEmpty()
                    .filterNot { it.snippet?.title?.contains("short", true) == true }
                    .mapNotNull {
                        val vid = it.contentDetails?.videoId ?: return@mapNotNull null
                        SearchSuggestionItem.VideoItem(
                            videoId       = vid,
                            title    = it.snippet?.title.orEmpty(),
                            subtitle = it.snippet?.description.orEmpty(),
                            thumbnailUrl = it.snippet?.thumbnails?.medium?.url,
                            snippet  = it.snippet!!
                        )
                    }
            } catch (e: HttpException) {
                val txt = e.response()?.errorBody().asString()
                Log.e("YTRepo", "searchKidsChannel failed\n$txt", e)
                emptyList()
            } catch (e: Exception) {
                Log.e("YTRepo", "getVideosFromChannel failed", e); emptyList()
            }
        }

    /*―――――――――――――――― helper mappers ――――――――――――――――*/
    private fun SearchItem.toChannelSuggestion(): SearchSuggestionItem.ChannelItem? {
        val chId = id?.channelId ?: return null
        return SearchSuggestionItem.ChannelItem(
            id    = chId,
            title        = snippet?.title.orEmpty(),
            subtitle     = snippet?.description.orEmpty(),
            thumbnailUrl = snippet?.thumbnails?.default?.url
        )
    }

    private fun SearchItem.toVideoSuggestion(): SearchSuggestionItem.VideoItem? {
        val vid = id?.videoId ?: return null
        return SearchSuggestionItem.VideoItem(
            videoId      = vid,
            title        = snippet?.title.orEmpty(),
            subtitle     = snippet?.description.orEmpty(),
            thumbnailUrl = snippet?.thumbnails?.medium?.url,
            snippet      = snippet!!
        )
    }
}