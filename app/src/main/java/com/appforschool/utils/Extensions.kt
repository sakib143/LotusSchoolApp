package com.appforschool.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.appforschool.R
import java.io.IOException
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Throws Exception from [Any] Object.
 */
fun <E : Throwable> Any.logException(
    exception: E,
    message: String = exception.message ?: "No message"
) = Log.e(
    this.javaClass.simpleName, message, exception
)

/**
 * Inflates view
 */
fun android.view.ViewGroup.inflate(layoutId: Int): View {
    return android.view.LayoutInflater.from(context).inflate(layoutId, this, false)
}

/**
 * Creates an [AutoClearedValue] associated with this fragment.
 */
fun <T : Any> Fragment.autoCleared() = AutoClearedValue<T>(this)

/**
 * Show Visibility of a [View]
 */
fun View.show() {
    this.visibility = View.VISIBLE
}

/**
 * Hide Visibility of a [View]
 */
fun View.hide() {
    this.visibility = View.GONE
}

/**
 * Invisible Visibility of a [View]
 */
fun View.invisible() {
    this.visibility = View.INVISIBLE
}

/**
 * Makes SnackBar which comes from Bottom for the Short duration
 */
fun makeSnackBar(text: String, view: View?) {
    val snackBar = view?.let { Snackbar.make(it, text, Snackbar.LENGTH_LONG) }
    snackBar?.show()
}

/**
 * this method is used for showing the keyboard
 */
fun View.showKeyboard() {

    if (requestFocus()) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }

}

/**
 * this method is used for hiding the keyboard into fragment
 */
fun Fragment.hideKeyboard() {
    val activity = this.activity
    if (activity is AppCompatActivity) {
        activity.hideKeyboard()
    }
}

/**
 * this method is used for showing the keyboard into activity
 */
fun AppCompatActivity.hideKeyboard() {
    val view = this.currentFocus
    if (view != null) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
    // else {
    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    // }
}

/**
 * this method is used for hiding the keyboard
 * */
fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

/**
 * Returns EdiText's text
 */
fun EditText.value() = text.toString().trim()

/**
 * this method is used for image loading from URL
 * */
@BindingAdapter("loadUrlImage","placeholderImage",requireAll = false)
fun loadUrlImage(view : ImageView, loadUrlImage : String?, placeholderImage : Int?) {
    Glide.with(view.context).load(loadUrlImage)
        .apply(
            RequestOptions()
                .priority(Priority.HIGH)
                .placeholder(placeholderImage!!)
                .error(R.mipmap.ic_launcher)
        )
        .into(view)
}

/**
 * this method is used for image loading from drawable
 * */
@BindingAdapter("loadDrawableImage")
fun loadDrawableImage(view: ImageView, drawable: Int?) {
    Glide.with(view.context).load(drawable)
        .apply(
            RequestOptions()
                .priority(Priority.HIGH)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
        )
        .into(view)
}

/**
 * this method is used for image loading from Assets
 * */
@BindingAdapter("loadimagFromAsset")
fun setImageFromAsset(view: ImageView, fileName: String) {
    try {
        val bitmap = BitmapFactory.decodeStream(view.context?.assets?.open(fileName))
        Glide.with(view.context).load(bitmap)
            .apply(
                RequestOptions()
                    .priority(Priority.HIGH)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
            )
            .into(view)
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

/**
 * this method is used for image loading from Assets
 * */
@BindingAdapter("loadImagFromBitmap")
fun setImageFromBitmap(view: ImageView, bitmap : Bitmap ) {
    try {
        view.setImageBitmap(bitmap)
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

fun EditText.setErrorWithFocus(message: String) {
    this.error = message
    this.requestFocus()
}

/**
 * this method is used for amount format
 * */
@BindingAdapter("amount")
fun setCurrencyAndAmount(textView: TextView, amount: String) {
    textView.text = "$".plus(DecimalFormat("0.00").format(amount.toDouble()))
}

/**
 * this method is used for date of birth format
 * */
@BindingAdapter("date")
fun setDateOfBirth(textView: TextView, dateStr: String) {
    val sdf = SimpleDateFormat("yy/MM/dd", Locale.getDefault())
    val output = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
    var date: Date? = null
    try {
        date = sdf.parse(dateStr)
        textView.text = output.format(date!!)
    } catch (e: Exception) {

    }

}

/**
 * this method is used for bottom navigation down animation
 * */
fun View.slideUp(duration: Int = 500) {
    visibility = View.VISIBLE
    val animate = TranslateAnimation(0f, 0f, this.height.toFloat(), 0f)
    animate.duration = duration.toLong()
    animate.fillAfter = true
    this.startAnimation(animate)
}

/**
 * this method is used for bottom navigation up animation
 * */
fun View.slideDown(duration: Int = 500) {
    visibility = View.VISIBLE
    val animate = TranslateAnimation(0f, 0f, 0f, this.height.toFloat())
    animate.duration = duration.toLong()
    animate.fillAfter = false
    this.startAnimation(animate)
}


fun Context.openDialPad(mobile: String) {
    val uri = "tel:$mobile"
    val intent = Intent(Intent.ACTION_DIAL)
    intent.data = Uri.parse(uri)
    startActivity(intent)
}

//
///*Show markerOnGoogleMap*/
//fun Context.openGoogleMap(latLng: LatLng, address: String?) {
//    try {
//        if (packageManager.getApplicationInfo("com.google.android.apps.maps", 0).enabled) {
//            val intent = Intent(
//                Intent.ACTION_VIEW,
//                Uri.parse("geo:${latLng.latitude.toString()},${latLng.longitude.toString()}?q=$address")
//            ).setPackage("com.google.android.apps.maps");
//            startActivity(intent);
//        }
//    } catch (e: PackageManager.NameNotFoundException) {
//    }
//}
//

/*get distance between two LatLng in miles */

fun getDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): String {
    var theta = lon1 - lon2;
    var dist = (Math.sin(deg2rad(lat1))
            * Math.sin(deg2rad(lat2))
            + Math.cos(deg2rad(lat1))
            * Math.cos(deg2rad(lat2))
            * Math.cos(deg2rad(theta)));
    dist = Math.acos(dist);
    dist = rad2deg(dist);
    dist = dist * 60 * 1.1515;
    return (DecimalFormat("#.#").format(dist) + " miles");
}

fun deg2rad(deg: Double): Double {
    return (deg * Math.PI / 180.0);
}

fun rad2deg(rad: Double): Double {
    return (rad * 180.0 / Math.PI);
}


/**
 * ScrollView scroll to bottom
 */
fun ScrollView.scrollToBottom() {
    val lastChild = getChildAt(childCount - 1)
    val bottom = lastChild.bottom + paddingBottom
    val delta = bottom - (scrollY + height)
    smoothScrollBy(0, delta)
}

/**
 * Set Image view tint
 */
fun ImageView.setTint(@ColorRes colorRes: Int) {
    ImageViewCompat.setImageTintList(
        this,
        ColorStateList.valueOf(ContextCompat.getColor(context, colorRes))
    )
}

fun SharedPreferences.Editor.putDouble(key: String, double: Double): SharedPreferences.Editor =
    putLong(key, java.lang.Double.doubleToRawLongBits(double))

fun SharedPreferences.getDouble(key: String, default: Double) =
    java.lang.Double.longBitsToDouble(getLong(key, java.lang.Double.doubleToRawLongBits(default)))


/**
 * Place cursor at the end of text in EditText
 */
fun EditText.placeCursorToEnd() {
    this.setSelection(this.text.length)
}

/**
 *  Extension method to get Date for String with specified format.
 */
fun Date.formatTo(dateFormat: String, timeZone: TimeZone = TimeZone.getDefault()): String {
    val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
    formatter.timeZone = timeZone
    return formatter.format(this)
}

/**
 * Extension method to get Date for String with specified format.
 */
fun String.dateInFormat(format: String, timeZone: TimeZone = TimeZone.getTimeZone("UTC")): Date? {
    val dateFormat = SimpleDateFormat(format, Locale.getDefault())
    dateFormat.timeZone = timeZone
    var parsedDate: Date? = null
    try {
        parsedDate = dateFormat.parse(this)
    } catch (ignored: ParseException) {
        ignored.printStackTrace()
    }
    return parsedDate
}

/**
 * Extension method to make text view link clickable
 */
fun TextView.makeLinks(vararg links: Pair<String, View.OnClickListener>) {
    val spannableString = SpannableString(this.text)
    for (link in links) {
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                Selection.setSelection((view as TextView).text as Spannable, 0)
                view.invalidate()
                link.second.onClick(view)
            }
        }
        val startIndexOfLink = this.text.toString().indexOf(link.first)
        spannableString.setSpan(
            clickableSpan, startIndexOfLink, startIndexOfLink + link.first.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    this.movementMethod =
        LinkMovementMethod.getInstance() // without LinkMovementMethod, link can not click
    this.setText(spannableString, TextView.BufferType.SPANNABLE)
}

/**
 * Extension method to set maxLength in EditText
 */
fun EditText.limitLength(maxLength: Int) {
    filters = arrayOf(InputFilter.LengthFilter(maxLength))
}

/**
 * Extension method to check valid email or not
 */
fun String.isEmailValid(): Boolean {
    return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun Activity.openGooglePlayApp(packageName : String){
    try {
        this.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
    } catch (anfe: ActivityNotFoundException) {
        this.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            )
        )
    }}


fun Context.toast(message:String) {
    val spannableStringBuilder = SpannableStringBuilder(message);
    spannableStringBuilder.setSpan(RelativeSizeSpan(1.35f), 0, message.length, 0)
    Toast.makeText(this,spannableStringBuilder, Toast.LENGTH_LONG).show()
}
