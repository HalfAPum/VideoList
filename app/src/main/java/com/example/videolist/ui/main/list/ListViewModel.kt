package com.example.videolist.ui.main.list

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.videolist.data.MainRepository
import com.example.videolist.models.Video
import com.example.videolist.utils.Constants.TAG
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import javax.inject.Inject

class ListViewModel @Inject constructor(private val mainRepository: MainRepository): ViewModel() {

    val videosObservable: LiveData<List<Video>>
        get() = mutableVideos

    private var mutableVideos = MutableLiveData<List<Video>>()

    val errorMessages : LiveData<String>
        get() = mainRepository.errorMessages

    /*
    Get videos and post them to livedata
    then try to load new videos
     */
    @SuppressLint("CheckResult")
    fun loadVideos(videos: List<Video>, path: File) {
        val videosObservable = mainRepository.getVideos()
        videosObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                mutableVideos.postValue(it)
            }
        mainRepository.loadVideos(videos, path)
    }
}