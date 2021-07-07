package com.example.videolist.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Video(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "video_name") val video_name: String?,
    @ColumnInfo(name = "video_path") val video_path: String?,
    @ColumnInfo(name = "image_name") val image_name: String?,
    @ColumnInfo(name = "image_path") val image_path: String?
)