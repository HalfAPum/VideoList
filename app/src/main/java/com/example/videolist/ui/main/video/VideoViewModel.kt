package com.example.videolist.ui.main.video

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.videolist.data.MainRepository
import com.example.videolist.models.Video
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class VideoViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {

    private val mutableVideo = MutableLiveData<List<Video>>()

    private val mutablePosition = MutableLiveData<Long>()

    private val mutableVideoId = MutableLiveData<Int>()

    val videos : LiveData<List<Video>> = mutableVideo

    val playbackPosition : LiveData<Long> = mutablePosition

    val videoId : LiveData<Int> = mutableVideoId

    //Get all videos
    fun getVideos() {
        val disposable = mainRepository.getVideos()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                mutableVideo.postValue(it)
            }
    }

    //Saves PlayerPosition
    fun savePlayerPosition(position: Long) {
        mutablePosition.postValue(position)
    }

    //Saves videoId
    fun saveVideoId(position: Int) {
        mutableVideoId.postValue(position)
    }
}