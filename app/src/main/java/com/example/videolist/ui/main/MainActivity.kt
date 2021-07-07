package com.example.videolist.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.videolist.R
import com.example.videolist.data.localsource.db.AppDatabase
import com.example.videolist.utils.Constants.TAG
import com.google.android.exoplayer2.MediaItem
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}