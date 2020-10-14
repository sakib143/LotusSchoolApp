package com.appforschool.ui.videocalling

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.facebook.react.modules.core.PermissionListener
import com.appforschool.R
import com.appforschool.base.BaseActivity
import com.appforschool.data.model.SetCallEndLogModel
import com.appforschool.utils.Constant
import com.appforschool.utils.toast
import org.jitsi.meet.sdk.*
import javax.inject.Inject
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions as JitsiMeetConferenceOptions1

class VideoCallingActivity : BaseActivity(), JitsiMeetActivityInterface {

    private var view: JitsiMeetView? = null
    private var roomUrl: String? = null
    private var userName: String? = null
    private var isHost: Int = 1
    private var scheduleId: Int = 0

    override fun layoutId() = R.layout.activity_video_calling

    @Inject
    lateinit var viewModel: VideoCallingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setObserver()
        getIntentData()
        setJitsiMeet()
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

    private fun setJitsiMeet() {
        val builder = JitsiMeetConferenceOptions1.Builder()
            .setFeatureFlag("invite.enabled", false)
            .setFeatureFlag("meeting-name.enabled", false)
            .setFeatureFlag("live-streaming.enabled", false)
            .setFeatureFlag("meeting-password.enabled", false)
            .setRoom(roomUrl)

        val jitsiMeetUserInfo = JitsiMeetUserInfo()
        jitsiMeetUserInfo.displayName = userName
        builder.setUserInfo(jitsiMeetUserInfo)
        //Camera off when user is NOT host
        if (isHost == 0) {
            builder.setVideoMuted(true)
        }

        val options = builder.build()

        view!!.join(options)
        view!!.listener = object : JitsiMeetViewListener {
            override fun onConferenceJoined(map: Map<String, Any>) {
            }

            override fun onConferenceTerminated(map: Map<String, Any>) {
                viewModel.executeSetEndcallLog(scheduleId)
            }

            override fun onConferenceWillJoin(map: Map<String, Any>) {
            }
        }
        setContentView(view)
    }

    private fun getIntentData() {
        view = JitsiMeetView(this)
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
        if (it.status) {
            toast(it!!.message)
        } else {
            toast(it!!.message)
        }
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
        view!!.dispose()
        view = null
        JitsiMeetActivityDelegate.onHostDestroy(this)
    }

    public override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        JitsiMeetActivityDelegate.onNewIntent(intent)
    }

    override fun requestPermissions(
        permissions: Array<String>,
        requestCode: Int,
        listener: PermissionListener
    ) {
        JitsiMeetActivityDelegate.requestPermissions(this, permissions, requestCode, listener)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        JitsiMeetActivityDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onResume() {
        super.onResume()
        //If user type is host then use below code otherwiae not
        if (isHost == 1) {
            JitsiMeetActivityDelegate.onHostResume(this)
        }

    }

    override fun onStop() {
        super.onStop()
        if (isHost == 1) {
            JitsiMeetActivityDelegate.onHostPause(this)
        }
    }


}