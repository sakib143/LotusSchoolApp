package com.appforschool.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Patterns
import androidx.core.app.ShareCompat
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.inject.Inject


class GlobalMethods @Inject constructor() {

    fun isPackageInstalled(
        packagename: String, context: Context
    ): Boolean {
        return try {
            context.packageManager.getPackageGids(packagename)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    fun getYoutubeThumbnailUrlFromVideoUrl(videoUrl: String): String? {
        return "http://img.youtube.com/vi/" + getYoutubeVideoIdFromUrl(videoUrl) + "/0.jpg"
    }

    fun getYoutubeVideoIdFromUrl(inUrl: String): String? {
        var inUrl = inUrl
        inUrl = inUrl.replace("&feature=youtu.be", "")
        if (inUrl.toLowerCase().contains("youtu.be")) {
            return inUrl.substring(inUrl.lastIndexOf("/") + 1)
        }
        val pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*"
        val compiledPattern: Pattern = Pattern.compile(pattern)
        val matcher: Matcher = compiledPattern.matcher(inUrl)
        return if (matcher.find()) {
            matcher.group()
        } else null
    }

    fun isInternetAvailable(context: Context): Boolean {
        var isOnline = false
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        try {
            val capabilities = manager.getNetworkCapabilities(manager.activeNetwork)
            isOnline =  capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return isOnline
    }

    fun drawableToBitmap(drawable: Drawable): Bitmap? {
        var bitmap: Bitmap? = null
        if (drawable is BitmapDrawable) {
            val bitmapDrawable = drawable
            if (bitmapDrawable.bitmap != null) {
                return bitmapDrawable.bitmap
            }
        }
        bitmap = if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
            Bitmap.createBitmap(
                1,
                1,
                Bitmap.Config.ARGB_8888
            ) // Single color bitmap will be created of 1x1 pixel
        } else {
            Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
        }
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
        drawable.draw(canvas)
        return bitmap
    }

    fun openFacebookPage(pageId: String, context: Context) {
        val pageUrl = "https://www.facebook.com/$pageId"
        try {
            val applicationInfo: ApplicationInfo =
                context.getPackageManager().getApplicationInfo("com.facebook.katana", 0)
            if (applicationInfo.enabled) {
                val versionCode: Int =
                    context.getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode
                val url: String
                url = if (versionCode >= 3002850) {
                    "fb://facewebmodal/f?href=$pageUrl"
                } else {
                    "fb://page/$pageId"
                }
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            } else {
                throw Exception("Facebook is disabled")
            }
        } catch (e: Exception) {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(pageUrl)))
        }
    }

    fun openTwitterPage(context: Context, userId: String) {
        var intent: Intent? = null
        try {
            context.getPackageManager().getPackageInfo("com.twitter.android", 0)
            intent = Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=$userId"))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        } catch (e: java.lang.Exception) {
            // no Twitter app, revert to browser
            intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://twitter.com/${Constant.TWITTER_NAME}")
            )
        }
        context.startActivity(intent)
    }

    fun openInstagram(context: Context, userId: String) {
        val uri = Uri.parse("http://instagram.com/_u/$userId")
        val likeIng = Intent(Intent.ACTION_VIEW, uri)
        likeIng.setPackage("com.instagram.android")
        try {
            context.startActivity(likeIng)
        } catch (e: ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/$userId")
                )
            )
        }
    }

    fun getFourDigitNumber(): String {
        val randomNumber = "" + ((Math.random() * 9000).toInt() + 1000)
        return "$randomNumber"
    }

    fun getRandomString(length: Int): String {
        val allowedChars = ('A'..'Z') + ('a'..'z')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    fun shareTextToFriend(context: Activity?, title: String?, text: String?) {
        ShareCompat.IntentBuilder.from(context!!)
            .setType("text/plain")
            .setChooserTitle(title)
            .setText(text)
            .startChooser()
    }

    fun shareAppToFriend(context: Activity, type: String?, title: String?, text: String) {
        ShareCompat.IntentBuilder.from(context)
            .setType(type)
            .setChooserTitle(title)
            .setText(text + context.packageName)
            .startChooser()
    }

    fun getAppVersion(context: Context): String {
        var version = ""
        try {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            version = pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return version
    }

    fun getDeviceName(): String =
        (if (Build.MODEL.startsWith(Build.MANUFACTURER, ignoreCase = true)) {
            Build.MODEL
        } else {
            "${Build.MANUFACTURER} ${Build.MODEL}"
        }).capitalize()

    // TODO: 28-09-2018 full image path to Bitmap by Sakib END
    fun getDeviceID(context: Context): String? {
        var android_id: String? = ""
        try {
            android_id =
                Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return android_id
    }

    fun isValidUrl(mData: String): Boolean {
        var isValidUrl: Boolean = Patterns.WEB_URL.matcher(mData).matches()
        return isValidUrl
    }

//    fun isToday(date: Date?): Boolean {
//        val today: Calendar = Calendar.getInstance()
//        val specifiedDate: Calendar = Calendar.getInstance()
//        specifiedDate.setTime(date)
//        return today.get(Calendar.DAY_OF_MONTH) === specifiedDate.get(Calendar.DAY_OF_MONTH) && today.get(
//            Calendar.MONTH
//        ) === specifiedDate.get(Calendar.MONTH) && today.get(Calendar.YEAR) === specifiedDate.get(
//            Calendar.YEAR
//        )
//    }

    fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap? {
        var width = image.width
        var height = image.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }

}