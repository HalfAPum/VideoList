package com.example.videolist.data.localsource

import android.util.Log
import com.example.videolist.data.localsource.db.DBHelper
import com.example.videolist.models.Video
import com.example.videolist.data.localsource.filesystem.FileSystem
import com.example.videolist.utils.Constants
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import java.io.File
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val fileSystem: FileSystem,
    private val dbHelper: DBHelper
){

    //Write file
    private fun writeFileToDisk(response: ResponseBody, path: File, name: String) : Boolean{
        return fileSystem.writeToDisk(response, path, name)
    }

    fun getVideo(video: Video) : Maybe<Video> {
        return dbHelper.getVideo(video.video_name!!)
    }

    fun getVideos() : Observable<List<Video>> {
        return dbHelper.getVideos()
    }

    //Insert video in db
    private fun insert(element: Video, path: String) {
        dbHelper.insertVideo(
            Video(
            element.id,
            element.video_name,
            path + element.video_name,
            element.image_name,
            path + element.image_name
        )
        )
    }

    fun saveVideo(imageBody: ResponseBody, videoBody: ResponseBody, path: File, element: Video) {
        Observable.fromCallable {
            val bool1 = writeFileToDisk(videoBody, path, element.video_name!!)
            val bool2 = writeFileToDisk(imageBody, path, element.image_name!!)
            if(bool1 || bool2) {
                insert(element, path.path + File.separator)
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }
}