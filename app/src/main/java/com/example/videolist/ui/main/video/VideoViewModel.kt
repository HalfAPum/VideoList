package com.example.videolist.ui.main.video

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.videolist.data.MainRepository
import com.example.videolist.models.Video
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class VideoViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {

    private val mutableVideo = MutableLiveData<List<Video>>()

    private val mutablePosition = MutableStateFlow(0L)

    private val mutableVideoId = MutableStateFlow(0)

    val videos : LiveData<List<Video>> = mutableVideo

    val playbackPosition : StateFlow<Long> = mutablePosition

    val videoId : StateFlow<Int> = mutableVideoId

    //Get all videos
    fun getVideos() {
        viewModelScope.launch {
            val videosFlow = mainRepository.getVideos()
            videosFlow.collect {
                mutableVideo.value = it
            }
        }
    }

    //Saves PlayerPosition
    fun savePlayerPosition(position: Long) {
        mutablePosition.value = position
    }

    //Saves videoId
    fun saveVideoId(position: Int) {
        mutableVideoId.value = position
    }
}