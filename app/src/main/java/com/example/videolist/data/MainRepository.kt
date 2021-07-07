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
    fun loadVideos(videos: List<Video>, path: File) {
        for (video in videos) {
            getVideo(video, path)
        }
    }

    //return all videos
    fun getVideos() : Observable<List<Video>> {
        return localDataSource.getVideos()
    }

    /*
    getVideo checks for video in db
    if it's doesn't exit then download it
    */
    private fun getVideo(element: Video, path: File) {
        localDataSource.getVideo(element)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : MaybeObserver<Video?> {
                lateinit var disposable: Disposable
                override fun onSubscribe(d: Disposable) {
                    disposable = d
                }

                override fun onSuccess(t: Video) {
                    disposable.dispose()
                }

                override fun onError(e: Throwable) {
                    Log.d(TAG, "$e")
                }

                override fun onComplete() {
                    downloadVideoFile(path, element)
                    disposable.dispose()
                }

            })
    }


    /*
    download video from network
    if video is valid then download image for this video
     */
    private fun downloadVideoFile(path: File, element: Video) {
        remoteDataSource.downloadFile(element.video_path!!)
            .subscribe(object : SingleObserver<retrofit2.Response<ResponseBody>>{
            lateinit var disposable: Disposable
            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

            override fun onSuccess(response: retrofit2.Response<ResponseBody>) {
                val responseBody = response.body()
                if(responseBody != null) {
                    downloadImageFile(responseBody, path, element)
                    Log.d(TAG, "downloaded and startingimega$element")
                    disposable.dispose()
                }
            }

            override fun onError(e: Throwable) {
                Log.d(TAG, e.toString())
                errorMessages.postValue("Check internet connection and try again")
                disposable.dispose()
            }

        })
    }

    /*
    download image from network
    if image is valid then save downloaded data
     */
    private fun downloadImageFile(videoFile: ResponseBody, path: File, element: Video) {
        remoteDataSource.downloadFile(element.image_path!!)
            .subscribe(object : SingleObserver<retrofit2.Response<ResponseBody>>{
                lateinit var disposable: Disposable
                override fun onSubscribe(d: Disposable) {
                    disposable = d
                }

                override fun onSuccess(response: retrofit2.Response<ResponseBody>) {
                    val responseBody = response.body()
                    if(responseBody != null) {
                        localDataSource.saveVideo(responseBody, videoFile, path, element)
                        disposable.dispose()
                    }
                }

                override fun onError(e: Throwable) {
                    Log.d(TAG, e.toString())
                    errorMessages.postValue("Check internet connection and try again")
                    disposable.dispose()
                }

            })
    }
}
