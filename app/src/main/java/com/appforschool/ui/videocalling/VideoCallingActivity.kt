package com.appforschool.ui.videocalling

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.facebook.react.modules.core.PermissionListener
import com.appforschool.R
import com.appforschool.base.BaseActivity
import com.appforschool.data.model.SetCallEndLogModel
import com.appforschool.utils.Constant
import com.appforschool.utils.toast
import org.jitsi.meet.sdk.*
import timber.log.Timber
import java.net.MalformedURLException
import java.net.URL
import javax.inject.Inject
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions as JitsiMeetConferenceOptions1

class VideoCallingActivity : BaseActivity() {

    private var userName: String? = null
    private var isHost: Int = 1
    private var scheduleId: Int = 0
    private var roomUrl: String? = null

    override fun layoutId() = R.layout.activity_video_calling

    @Inject
    lateinit var viewModel: VideoCallingViewModel

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            onBroadcastReceived(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setObserver()
        getIntentData()
        setJitsiMeet()
    }

    private fun setJitsiMeet() {
        val serverURL: URL
        serverURL = try {
            URL(roomUrl)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
            throw RuntimeException("Invalid server URL!")
        }

        var builder = JitsiMeetConferenceOptions1.Builder()
            .setServerURL(serverURL)
            .setWelcomePageEnabled(false)
            .setFeatureFlag("invite.enabled", false)
            .setFeatureFlag("meeting-name.enabled", false)
            .setFeatureFlag("live-streaming.enabled", false)
            .setFeatureFlag("meeting-password.enabled", false)
            .setFeatureFlag("video-share.enabled", false)   // Hide Youtube option from list.
            .setFeatureFlag("close-captions.enabled", false) // Hide Start showing subtitles from list.

        val jitsiMeetUserInfo = JitsiMeetUserInfo()
        jitsiMeetUserInfo.displayName = userName
        builder.setUserInfo(jitsiMeetUserInfo)

        //Camera off when user is NOT host
        if (isHost == 0) {
            builder.setVideoMuted(true)
        }

        JitsiMeet.setDefaultConferenceOptions(builder.build())
        registerForBroadcastMessages()

        builder.build()

        val options = org.jitsi.meet.sdk.JitsiMeetConferenceOptions.Builder()
            .setRoom(scheduleId.toString())
            .build()
        JitsiMeetActivity.launch(this, options)
    }

    companion object {
        @JvmStatic
        fun intentFor(
            context: Context,
            roomId: String,
            scheduleId: Int
        ) =
            Intent(context, VideoCallingActivity::class.java)
                .putExtra(Constant.KEY_ROOM_URL, roomId)
                .putExtra(Constant.REQUEST_SCHEDULE_ID, scheduleId)
    }

    private fun getIntentData() {
        roomUrl = intent.getStringExtra(Constant.KEY_ROOM_URL)
        userName = prefUtils.getUserData()?.studentname
        scheduleId = intent.getIntExtra(Constant.REQUEST_SCHEDULE_ID, 0)
        isHost = prefUtils.getUserData()?.ishost!!
    }

    private fun setObserver() {
        viewModel.onMessageError.observe(this, onMessageErrorObserver)
        viewModel.call_end_log.observe(this, callEndObserver)
    }

    private val onMessageErrorObserver = Observer<Any> {
        toast(it.toString())
    }

    private val callEndObserver = Observer<SetCallEndLogModel> {
        toast(it!!.message)
        finish()
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        JitsiMeetActivityDelegate.onActivityResult(
            this, requestCode, resultCode, data
        )
    }

    override fun onBackPressed() {
        JitsiMeetActivityDelegate.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
        JitsiMeetActivityDelegate.onHostDestroy(this)
    }

    public override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        JitsiMeetActivityDelegate.onNewIntent(intent)
    }

    // Screen sharing relates stuff by Sakib Syed START
    private fun onBroadcastReceived(intent: Intent?) {
        if (intent != null) {
            val event = BroadcastEvent(intent)
            when (event.getType()) {
                BroadcastEvent.Type.CONFERENCE_JOINED -> Timber.i(
                    "Conference Joined with url%s",
                    event.getData().get("url")
                )
                BroadcastEvent.Type.PARTICIPANT_JOINED -> Timber.i(
                    "Participant joined%s",
                    event.getData().get("name")
                )
                BroadcastEvent.Type.PARTICIPANT_LEFT ->
                    viewModel.executeSetEndcallLog(scheduleId)
            }
        }
    }

    private fun registerForBroadcastMessages() {
        val intentFilter = IntentFilter()
        for (type in BroadcastEvent.Type.values()) {
            intentFilter.addAction(type.action)
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter)
    }
    // Screen sharing relates stuff by Sakib Syed END

}