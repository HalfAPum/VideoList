package com.example.videolist.data.remotesource

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import com.example.videolist.network.main.MainApi
import com.example.videolist.utils.Constants.TAG
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val mainApi: MainApi){

    //Download video on back thread using retrofit2
    suspend fun downloadFile(url: String): Response<ResponseBody> {
        return mainApi.downloadVideo(url)
    }
}