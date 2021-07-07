package com.example.videolist.di.main

import com.example.videolist.data.MainRepository
import com.example.videolist.data.localsource.LocalDataSource
import com.example.videolist.data.localsource.db.DBHelper
import com.example.videolist.data.localsource.filesystem.FileSystem
import com.example.videolist.data.remotesource.RemoteDataSource
import com.example.videolist.ui.main.list.ListFragment
import com.example.videolist.ui.main.video.VideoFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentsBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributesListFragment(): ListFragment

    @ContributesAndroidInjector
    abstract fun contributesVideoFragment(): VideoFragment

}
