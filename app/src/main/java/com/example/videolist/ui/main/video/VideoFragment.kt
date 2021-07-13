package com.example.videolist.ui.main.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.videolist.R
import com.example.videolist.ViewModelProviderFactory
import com.example.videolist.adapters.DescriptionAdapter
import com.example.videolist.databinding.FragmentVideoBinding
import com.example.videolist.models.Video
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.util.Util
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.properties.Delegates


class VideoFragment : DaggerFragment() {

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    private lateinit var viewModel: VideoViewModel

    private var _binding: FragmentVideoBinding? = null

    private val binding get() = _binding!!

    //exoplayer variables
    private var player : SimpleExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow : Int = 0
    private var playbackPosition : Long = 0
    private var videoId = 0
    private val bundleKey = "VideoId"

    //variables for exoplayer notification
    private lateinit var playerNotificationManager: PlayerNotificationManager
    private val channelId = "Channel_id"
    private val notificationId = 123

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this, providerFactory).get(VideoViewModel::class.java)
        subscribeObservers()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializePlayerNotificationManager()
    }

    private fun initializePlayerNotificationManager() {
        playerNotificationManager = PlayerNotificationManager.Builder(
            requireContext(),
            notificationId,
            channelId,
            DescriptionAdapter()
        ).build()
        playerNotificationManager.setUseNextActionInCompactView(true)
        playerNotificationManager.setUsePreviousActionInCompactView(true)
    }

    private fun subscribeObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.playbackPosition.collect {
                    playbackPosition = it
                }
                viewModel.videoId.collect {
                    videoId = it
                }
            }
        }
        //Observe videosList
        viewModel.videos.observe(viewLifecycleOwner) {
            setupPlayer(it)
        }
    }

    private fun setupPlayer(it: List<Video>) {
        for (video in it) {
            val mediaItem: MediaItem = MediaItem.fromUri(video.video_path!!)
            player!!.addMediaItem(mediaItem)
        }
        player!!.playWhenReady = playWhenReady
        player!!.seekTo(videoId, playbackPosition)
        player!!.prepare()
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT < 24 || player == null) {
            initializePlayer()
        }
    }

    private fun initializePlayer() {
        player = SimpleExoPlayer.Builder(requireContext()).build()
        playerNotificationManager.setPlayer(player)
        binding.videoView.player = player
        videoId = arguments?.getInt(bundleKey)!!
        viewModel.getVideos()
    }


    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            savePlayerProperties()
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            savePlayerProperties()
            releasePlayer()
        }
    }

    private fun savePlayerProperties() {
        viewModel.savePlayerPosition(player!!.currentPosition)
        viewModel.saveVideoId(player!!.currentWindowIndex)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun releasePlayer() {
        if (player != null) {
            playWhenReady = player!!.playWhenReady
            playbackPosition = player!!.currentPosition
            currentWindow = player!!.currentWindowIndex
            playerNotificationManager.setPlayer(null)
            player!!.release()
            player = null
        }
    }
}