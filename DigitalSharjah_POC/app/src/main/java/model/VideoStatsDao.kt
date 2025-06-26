package model

import androidx.room.*

@Dao
interface VideoStatsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStats(stats: List<VideoStatsEntity>) // ✅ Fix: No return type needed

    @Query("SELECT * FROM video_stats")
    suspend fun getAllStats(): List<VideoStatsEntity> // ✅ Fix: Return List of VideoStatsEntity

    @Query("DELETE FROM video_stats")
    suspend fun clearStats() // ✅ Fix: No return type needed
}
