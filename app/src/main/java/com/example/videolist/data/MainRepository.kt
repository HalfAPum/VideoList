package com.example.videolist.data

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.videolist.data.localsource.LocalDataSource
import com.example.videolist.models.Video
import com.example.videolist.data.remotesource.RemoteDataSource
import com.example.videolist.utils.Constants.TAG
import io.reactivex.MaybeObserver
import io.reactivex.Observable
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import java.io.File
import javax.inject.Inject

//class represents Model
class MainRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
) {

    val errorMessages = MutableLiveData<String>()

    /*
    Check that all videos are in db
    download if not
    */
    suspend fun loadVideos(videos: List<Video>, path: File) {
        withContext(Dispatchers.IO) {
            for (video in videos) {

                    getVideo(video, path)

            }
        }
    }

    //return all videos
    fun getVideos() : Flow<List<Video>> {
        return localDataSource.getVideos()
    }

    /*
    getVideo checks for video in db
    if it's doesn't exit then download it
    */
    private suspend fun getVideo(element: Video, path: File) {
        val video = localDataSource.getVideo(element)
        if (video == null) {
            downloadVideoFile(path, element)
        }
    }


    /*
    download video from network
    if video is valid then download image for this video
     */
    private suspend fun downloadVideoFile(path: File, element: Video) {
        val result = remoteDataSource.downloadFile(element.video_path!!).body()
        if(result != null) {
            downloadImageFile(result, path, element)
        }
    }

    /*
    download image from network
    if image is valid then save downloaded data
     */
    private suspend fun downloadImageFile(videoFile: ResponseBody, path: File, element: Video) {
        val result = remoteDataSource.downloadFile(element.image_path!!).body()
        if(result != null) {
            localDataSource.saveVideo(result, videoFile, path, element)
        }
    }
}

