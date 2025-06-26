package com.safeplay.kids.pojo

import com.google.gson.annotations.SerializedName

/** One sealed class that covers *both* channel & video suggestions. */
sealed class SearchSuggestionItem {

    abstract val title:        String
    abstract val subtitle:     String           // channel-title | video-desc
    abstract val thumbnailUrl: String?

    //─────────────── CHANNEL ───────────────//
    data class ChannelItem(
        val id: String, // ← Make sure this line exists
        override val title: String,
        override val subtitle: String,
        override val thumbnailUrl: String?
    ) : SearchSuggestionItem()

    //─────────────── VIDEO ───────────────//
    data class VideoItem(
        val videoId: String,
        override val title:        String,
        override val subtitle:     String,
        override val thumbnailUrl: String?,
        val snippet:     Snippet
    ) : SearchSuggestionItem()
}

data class VideoId(val videoId: String)


/** Top-level response of the /search endpoint                                 */
data class YouTubeSearchResponse(
    val items: List<SearchItem>?
)