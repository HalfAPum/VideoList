package com.example.videolist.di.main

import com.example.videolist.data.MainRepository
import com.example.videolist.data.localsource.LocalDataSource
import com.example.videolist.data.localsource.db.DBHelper
import com.example.videolist.data.localsource.filesystem.FileSystem
import com.example.videolist.data.remotesource.RemoteDataSource
import dagger.Module

@Module
abstract class MainDataBuildersModule {

    abstract fun mainRepository(): MainRepository

    abstract fun remoteDataSource(): RemoteDataSource

    abstract fun remoteLocalSource(): LocalDataSource

    abstract fun fileSystem(): FileSystem

    abstract fun dbHelper(): DBHelper
}