package com.example.videolist.network.main

import io.reactivex.Flowable
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Streaming

//Provides connection to network
interface MainApi {

    @GET("{url}")
    fun downloadVideo(@Path("url") url: String) : Single<Response<ResponseBody>>

}