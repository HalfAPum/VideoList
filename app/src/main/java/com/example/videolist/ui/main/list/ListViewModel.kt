package com.example.videolist.ui.main.list

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.videolist.data.MainRepository
import com.example.videolist.models.Video
import com.example.videolist.utils.Constants.TAG
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

class ListViewModel @Inject constructor(private val mainRepository: MainRepository): ViewModel() {

    val videosObservable: LiveData<List<Video>>
        get() = mutableVideos

    private var mutableVideos = MutableLiveData<List<Video>>()

    val errorMessages : LiveData<String>
        get() = mainRepository.errorMessages

    private val tasks = ArrayList<Deferred<Unit>>()

    /*
    Get videos and post them to livedata
    then try to load new videos
     */
    fun loadVideos(videos: List<Video>, path: File) {

        viewModelScope.launch {
            val videosFlow = mainRepository.getVideos()
            var result = async {
                videosFlow.collect {
                    mutableVideos.value = it
                }
            }
            tasks.add(result)
            result = async {
                mainRepository.loadVideos(videos, path)
            }
            tasks.add(result)
        }
    }

    override fun onCleared() {
        super.onCleared()
        tasks.forEach {
            it.cancel()
        }
    }
}