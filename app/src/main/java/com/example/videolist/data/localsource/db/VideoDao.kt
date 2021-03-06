package com.example.videolist.data.localsource.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.videolist.models.Video
import io.reactivex.Maybe
import io.reactivex.Observable
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoDao {

    @Query("SELECT * FROM video")
    fun getAllVideos(): Flow<List<Video>>

    @Query("SELECT * FROM video WHERE video_name = :name")
    fun getVideo(name: String): Video?

    @Insert
    fun insert(video: Video)
}