package com.example.videolist.data.localsource.db

import com.example.videolist.models.Video
import io.reactivex.*
import javax.inject.Inject

//Helper gor room db
class DBHelper @Inject constructor(appDatabase: AppDatabase) {

    private val videoDao =  appDatabase.videoDao()

    fun getVideo(name: String) : Maybe<Video> {
        return videoDao.getVideo(name)
    }

    fun insertVideo(video: Video) {
        videoDao.insert(video)
    }

    fun getVideos() : Observable<List<Video>> {
        return videoDao.getAllVideos()
    }
}