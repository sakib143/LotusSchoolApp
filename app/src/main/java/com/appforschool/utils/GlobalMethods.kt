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
import android.net.Uri
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
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE)
                    as ConnectivityManager
        connectivityManager.activeNetworkInfo.also {
            return it != null && it.isConnected
        }
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

     fun getFourDigitNumber() : String {
         val randomNumber  = "" + ((Math.random() * 9000).toInt() + 1000)
         return "$randomNumber"
     }

    fun getRandomString(length: Int) : String {
        val allowedChars = ('A'..'Z') + ('a'..'z')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    fun shareTextToFriend(context: Activity?,  title: String?, text: String?) {
        ShareCompat.IntentBuilder.from(context!!)
            .setType("text/plain")
            .setChooserTitle(title)
            .setText(text)
            .startChooser()
    }

}