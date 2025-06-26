package com.safeplay.kids.pojo

import com.google.gson.annotations.SerializedName

/**
 * Minimal wrapper for the `/search` endpoint when the **type = channel**.
 * Only the fields we actually consume are mapped.
 */
data class SearchResultItem(
    val id: SearchId?            /* contains channelId for channels            */,
    val snippet: Snippet?        /* title / etc. – already defined elsewhere   */
)

/** A single item in that list.                                                */
data class SearchItem(
    val id:      SearchId?,      // contains either channelId or videoId
    val snippet: Snippet?
)

/** “id” object of an item (kind tells what it is).                            */
data class SearchId(
    val kind:      String?  = null,   // e.g. youtube#channel | youtube#video
    val channelId: String?  = null,
    val videoId:   String?  = null
)