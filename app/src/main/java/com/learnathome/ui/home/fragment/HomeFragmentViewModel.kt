package com.learnathome.ui.home.fragment

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.learnathome.MyApp
import com.learnathome.utils.PrefUtils
import javax.inject.Inject

class HomeFragmentViewModel  @Inject constructor(
    private val application: MyApp,
    private val prefUtils: PrefUtils
) : AndroidViewModel(application) {



}