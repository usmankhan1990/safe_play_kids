package com.safeplay.kids.pojo

data class ChannelResponse(
    val items: List<ChannelItem>?
)

data class ChannelItem(
    val contentDetails: ChannelContentDetails?
)

data class ChannelContentDetails(
    val relatedPlaylists: RelatedPlaylists?
)

data class RelatedPlaylists(
    val uploads: String?        // uploads playlist id
)

/* ----------------------------- playlistItems ------------------------------ */
data class PlaylistItemsResponse(
    val items: List<PlaylistItem>?
)

data class PlaylistItem(
    val snippet:        Snippet?,
    val contentDetails: PlaylistContent?
)

data class PlaylistContent(
    val videoId: String?
)
