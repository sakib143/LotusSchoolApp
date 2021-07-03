package com.appforschool.ui.youtube

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.WindowManager
import androidx.annotation.NonNull
import com.appforschool.R
import com.appforschool.base.BaseActivity
import com.appforschool.utils.Constant
import com.appforschool.utils.toast
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener
import kotlinx.android.synthetic.main.activity_youtube.*
import java.util.regex.Pattern

class YoutubeActivity : BaseActivity() {

    override fun layoutId() = R.layout.activity_youtube
    private var youtube_url = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        //Hide statusbar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        youtube_url = intent.getStringExtra(Constant.VIDEO_URL)!!

        youtube_player_view.addYouTubePlayerListener(object :
            AbstractYouTubePlayerListener() {
            override fun onReady(@NonNull youTubePlayer: YouTubePlayer) {
//                val videoId = extractYoutubeVideoId(youtube_url)

                val videoId = globalMethods.extractYoutubeVideoId(youtube_url)

                if (videoId!=null) {
                    youTubePlayer.play()
                    youTubePlayer.loadVideo(videoId!!, 0f)
                }
                else{
                    toast("Not Get Proper Url")
                }
            }
        })

        youtube_player_view.enterFullScreen()

        youtube_player_view.addFullScreenListener(object : YouTubePlayerFullScreenListener {
            override fun onYouTubePlayerEnterFullScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }

            override fun onYouTubePlayerExitFullScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        })

        if(youtube_player_view.isFullScreen()){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        youtube_player_view.enableBackgroundPlayback(false)
    }

    companion object {
        @JvmStatic
        fun intentFor(context: Context, videoUrls : String) =
            Intent(context, YoutubeActivity::class.java)
                .putExtra(Constant.VIDEO_URL, videoUrls)
    }

    override fun onDestroy() {
        super.onDestroy()
        youtube_player_view.release()
    }

}