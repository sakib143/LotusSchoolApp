package com.appforschool.ui.videocalling

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.facebook.react.modules.core.PermissionListener
import com.appforschool.R
import com.appforschool.utils.Constant
import org.jitsi.meet.sdk.*

class VideoCallingActivity: FragmentActivity(), JitsiMeetActivityInterface {

    private var view: JitsiMeetView? = null
    private var roomUrl: String? = null
    private var userName: String? = null
    private var isHost: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_calling)

        view = JitsiMeetView(this)
        roomUrl = intent.getStringExtra(Constant.KEY_ROOM_URL)
        userName = intent.getStringExtra(Constant.REQUEST_USERNAME)
        isHost = intent.getIntExtra(Constant.IS_HOST,0)

        val builder = JitsiMeetConferenceOptions.Builder()
            .setFeatureFlag("invite.enabled", false)
            .setFeatureFlag("meeting-name.enabled", false)
            .setFeatureFlag("live-streaming.enabled", false)
            .setFeatureFlag("meeting-password.enabled", false)
            .setRoom(roomUrl)
        val jitsiMeetUserInfo = JitsiMeetUserInfo()
        jitsiMeetUserInfo.displayName = userName
        builder.setUserInfo(jitsiMeetUserInfo)
        val options = builder.build()
        view!!.join(options)
        view!!.listener = object : JitsiMeetViewListener {
            override fun onConferenceJoined(map: Map<String, Any>) {
                Log.e("onConferenceJoined", map.toString())
            }

            override fun onConferenceTerminated(map: Map<String, Any>) {
                finish()
            }

            override fun onConferenceWillJoin(map: Map<String, Any>) {
                Log.e("onConferenceWillJoin", map.toString())
            }
        }
        setContentView(view)
    }

    companion object {
        @JvmStatic
        fun intentFor(context: Context, roomId: String, userName: String,ishost: Int) =
            Intent(context, VideoCallingActivity::class.java)
                .putExtra(Constant.KEY_ROOM_URL, roomId)
                .putExtra(Constant.REQUEST_USERNAME, userName)
                .putExtra(Constant.IS_HOST, ishost)
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

    override fun requestPermissions(permissions: Array<String>, requestCode: Int, listener: PermissionListener) {
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
        if(isHost == 1){
            JitsiMeetActivityDelegate.onHostResume(this)
        }

    }

    override fun onStop() {
        super.onStop()
        if(isHost == 1){
            JitsiMeetActivityDelegate.onHostPause(this)
        }
    }
}