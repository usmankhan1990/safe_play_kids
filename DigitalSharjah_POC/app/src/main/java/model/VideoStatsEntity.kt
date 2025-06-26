package model

import android.content.Context
import androidx.room.*
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Entity(tableName = "video_stats")
data class VideoStatsEntity(
    @PrimaryKey val videoId: String,
    val title: String,
    val views: Int,
    val watchHours: Int,
    val subscriptions: Int
)

