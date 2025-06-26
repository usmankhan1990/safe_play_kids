package pojo

data class YouTubeChannelResponse(
    val items: List<ChannelItem>
)

data class ChannelItem(
    val statistics: ChannelStatistics
)

data class ChannelStatistics(
    val viewCount: String,
    val subscriberCount: String
)

data class YouTubeVideoResponse(
    val items: List<VideoItem>
)

data class VideoItem(
    val id: String,
    val snippet: VideoSnippet,
    val statistics: VideoStatistics
)

data class VideoSnippet(
    val title: String
)

data class VideoStatistics(
    val viewCount: String
)
