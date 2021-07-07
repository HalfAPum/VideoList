package com.example.videolist.di.main

import androidx.room.Room
import com.example.videolist.data.localsource.db.AppDatabase
import com.example.videolist.network.main.MainApi
import com.example.videolist.ui.main.MainActivity
import com.example.videolist.adapters.RecyclerViewAdapter
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class MainModule {

    companion object {
        @MainScope
        @Provides
        fun provideRecyclerViewAdapter() =
            RecyclerViewAdapter()

        @MainScope
        @Provides
        fun provideMainApi(retrofit: Retrofit): MainApi{
            return retrofit.create(MainApi::class.java)
        }

        @MainScope
        @Provides
        fun provideRoomInstance(mainActivity: MainActivity) : AppDatabase{
            return Room.databaseBuilder(
                mainActivity,
                AppDatabase::class.java, "VideosDB32"
            )
                .build()
        }

    }

}