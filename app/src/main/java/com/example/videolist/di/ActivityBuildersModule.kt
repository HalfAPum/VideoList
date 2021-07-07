package com.example.videolist.di

import com.example.videolist.di.main.*
import com.example.videolist.ui.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @MainScope
    @ContributesAndroidInjector(modules = [
        MainModule::class,
        MainViewModelsModule::class,
        MainFragmentsBuildersModule::class,
        MainDataBuildersModule::class
    ])
    abstract fun contributeMainActivity() : MainActivity
}