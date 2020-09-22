package com.appforschool.utils.databindingadapter

import android.graphics.BitmapFactory
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.webkit.WebView
import android.widget.EditText
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.appforschool.R
import java.io.IOException


@BindingAdapter("imagFromAsset")
fun setImageFromAsset(view: ImageView, fileName: String) {
    try {
        val bitmap = BitmapFactory.decodeStream(view.context?.assets?.open(fileName))
        view.setImageBitmap(bitmap)
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

@BindingAdapter("imagUrl", "placeholderImage", "errorImage", requireAll = false)
fun loadImageFromUrl(view: ImageView, url: String, isPlacehoder: Boolean, isError: Boolean) {
    val requestOption: RequestOptions = RequestOptions()
    if (isPlacehoder) {
        requestOption.placeholder(R.mipmap.ic_launcher)
    }

    if (isError) {
        requestOption.placeholder(R.mipmap.ic_launcher)
    }

    Glide.with(view.context)
        .load(url)
        .into(view)
}

@BindingAdapter("webviewUrl")
fun WebView.setUrl(url: String) {
    this.loadUrl(url)
}

@BindingAdapter("hideShowPassword")
fun setHideShowPassword(editext: EditText, isShow: Boolean) {
    if (isShow) {
        editext.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
    } else {
        editext.setTransformationMethod(PasswordTransformationMethod.getInstance())
    }
}
