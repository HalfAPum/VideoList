package com.example.videolist.data.localsource.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.videolist.models.Video

@Database(entities = [Video::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun videoDao() : VideoDao
}