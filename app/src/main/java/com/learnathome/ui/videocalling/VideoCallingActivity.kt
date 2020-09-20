package com.learnathome.ui.videocalling

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.facebook.react.modules.core.PermissionListener
import com.learnathome.R
import com.learnathome.utils.Constant
import org.jitsi.meet.sdk.*

class VideoCallingActivity: FragmentActivity(), JitsiMeetActivityInterface {

    private var view: JitsiMeetView? = null
    private var roomUrl: String? = null
    private var userName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_calling)

        view = JitsiMeetView(this)
        roomUrl = intent.getStringExtra(Constant.KEY_ROOM_URL)
        userName = intent.getStringExtra(Constant.REQUEST_USERNAME)

        val builder = JitsiMeetConferenceOptions.Builder()
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
        fun intentFor(context: Context, roomId: String, userName: String) =
            Intent(context, VideoCallingActivity::class.java)
                .putExtra(Constant.KEY_ROOM_URL, roomId)
                .putExtra(Constant.REQUEST_USERNAME, userName)
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
        JitsiMeetActivityDelegate.onHostResume(this)
    }

    override fun onStop() {
        super.onStop()
        JitsiMeetActivityDelegate.onHostPause(this)
    }
}