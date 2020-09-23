package com.appforschool.ui.commonwebview

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.appforschool.MyApp
import com.appforschool.utils.PrefUtils
import javax.inject.Inject

class CommonWebviewViewModel  @Inject constructor(
    private val prefUtils: PrefUtils,
    private val application: MyApp
) : AndroidViewModel(application) {

    val webviewUrl: MutableLiveData<String> = MutableLiveData<String>()

}