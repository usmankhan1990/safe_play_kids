package com.safeplay.kids.pojo

import com.google.gson.annotations.SerializedName

/**  *Single* snippet model – used everywhere.  */
data class Snippet(
    @SerializedName("title")       val title:       String?,
    @SerializedName("description") val description: String?,
    @SerializedName("channelId")   val channelId:   String?,
    @SerializedName("thumbnails")  val thumbnails:  Thumbnails?
)

data class Thumbnails(
    val default : Thumbnail?,   // 120×90
    val medium  : Thumbnail?,   // 320×180
    val high    : Thumbnail?    // 480×360
)

data class Thumbnail(
    val url: String?
)