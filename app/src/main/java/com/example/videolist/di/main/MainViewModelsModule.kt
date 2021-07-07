package com.example.videolist.di.main

import androidx.lifecycle.ViewModel
import com.example.videolist.di.ViewModelKey
import com.example.videolist.ui.main.list.ListViewModel
import com.example.videolist.ui.main.video.VideoViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(ListViewModel::class)
    abstract fun bindMainViewModel(listViewModel: ListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(VideoViewModel::class)
    abstract fun bindVideoViewModel(videoViewModel: VideoViewModel): ViewModel

}