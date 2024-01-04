package com.gontory.elqirasygontory.ui.materi

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.navArgs
import com.gontory.elqirasygontory.databinding.ActivityMateriBinding
import com.gontory.elqirasygontory.utils.Resource
import com.gontory.elqirasygontory.ui.mutholaah.MutholaahViewModel
import kotlinx.coroutines.flow.collectLatest

class MateriActivity : AppCompatActivity() {

    private val args by navArgs<MateriActivityArgs>()
    private lateinit var binding: ActivityMateriBinding
    private val mutholaahViewModel by viewModels<MutholaahViewModel>()

    private var player: ExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMateriBinding.inflate(layoutInflater)
        setContentView(binding.root)

        customToolbar()
        fullScreen()
        retrieveVideo()


        if (!isNetworkAvailable()) {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show()
        }


    }

    private fun retrieveVideo() {
        lifecycleScope.launchWhenStarted {
            mutholaahViewModel.mutholaahList.collectLatest { resource ->
                when(resource) {
                    is Resource.Success -> {
                        val mutholaahList = resource.data
                        val mutholaah = mutholaahList?.find { it.urutan == args.mutholaah.urutan }

                        if (mutholaah != null) {
                            binding.apply {
                                videoMutholaah.visibility = View.VISIBLE
                                noData.visibility = View.GONE
                            }

                            initializeVideo(mutholaah.video_url)
                        } else {
                            // No Data layout
                            binding.apply {
                                videoMutholaah.visibility = View.INVISIBLE
                                noData.visibility = View.VISIBLE
                            }
                        }
                    }

                    is Resource.Error -> {
                        Toast.makeText(this@MateriActivity, "Error: ${resource.message}", Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectionManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectionManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun fullScreen() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, binding.videoMutholaah).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

//    private fun initializeVideo(videoUrl: String) {
//        releasePlayer()
//
//        val player = ExoPlayer.Builder(this).build().apply {
//            setMediaItem(MediaItem.fromUri(videoUrl))
//            prepare()
//            play()
//        }
//        binding.videoMutholaah.player = player
//    }

    private fun initializeVideo(videoUrl: String) {
        releasePlayer()

        player = ExoPlayer.Builder(this).build().apply {
            setMediaItem(MediaItem.fromUri(videoUrl))
            prepare()
            play()
        }
        binding.videoMutholaah.player = player
    }


    private fun customToolbar() {
        binding.apply {
            toolbar.navBack.setOnClickListener {
                onBackPressed()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    override fun onBackPressed() {
        releasePlayer()
        super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun releasePlayer() {
        player?.release()
        player = null
    }

}