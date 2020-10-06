package com.appforschool.ui.videoplaying

import android.content.pm.ActivityInfo
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.appforschool.R
import com.appforschool.base.BaseActivity
import com.appforschool.utils.Constant
import com.appforschool.utils.exoplayer_utils.BaseVideoActivity
import com.appforschool.utils.exoplayer_utils.DataAndUtils
import com.appforschool.utils.exoplayer_utils.YouTubeOverlay
import com.google.android.exoplayer2.ui.PlayerView.SHOW_BUFFERING_WHEN_PLAYING
import kotlinx.android.synthetic.main.activity_video_playing2.*
import kotlinx.android.synthetic.main.exo_playback_control_view_yt.*

class VideoPlayingActivity : BaseVideoActivity() {

    private var isVideoFullscreen = false
    private var currentVideoId = -1
    private var url: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_playing2)

        url = intent.getStringExtra(Constant.VIDEO_URL)

        this.videoPlayer = previewPlayerView
        initDoubleTapPlayerView()
        startNextVideo()

        fullscreen_button.setOnClickListener {
            toggleFullscreen()
        }

        fullscreen_button.performClick()
        isVideoFullscreen = true
    }

    private fun initDoubleTapPlayerView() {
        ytOverlay
            .performListener(object : YouTubeOverlay.PerformListener {
                override fun onAnimationStart() {
                    previewPlayerView.useController = false
                    ytOverlay.visibility = View.VISIBLE
                }
                override fun onAnimationEnd() {
                    ytOverlay.visibility = View.GONE
                    previewPlayerView.useController = true
                }
            })

        previewPlayerView.doubleTapDelay = 800
    }

    private fun startNextVideo() {
        releasePlayer()
        initializePlayer()
        ytOverlay.player(player!!)

        currentVideoId = (currentVideoId + 1).rem(DataAndUtils.videoList.size)
        buildMediaSource(Uri.parse(url))
    }

    private fun toggleFullscreen() {
        if (isVideoFullscreen) {
            setFullscreen(false)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE;
            if(supportActionBar != null){
                supportActionBar?.show();
            }
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            isVideoFullscreen = false
        } else {
            setFullscreen(true)
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                    and View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    and View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)

            if(supportActionBar != null){
                supportActionBar?.hide();
            }
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            isVideoFullscreen = true
        }
    }

    override fun onBackPressed() {
        if (isVideoFullscreen) {
            toggleFullscreen()
            return
        }
        super.onBackPressed()
    }
}