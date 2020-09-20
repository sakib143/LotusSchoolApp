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

//    private var _userName = MutableLiveData<String>()
//    val userName: LiveData<String> get() = _userName
//
//    private var _user_profile_image = MutableLiveData<String>()
//    val user_profile_image: LiveData<String> get() = _user_profile_image
//
//    private var _standardname = MutableLiveData<String>()
//    val standardname: LiveData<String> get() = _standardname
//
//    val userPlaceHolder = R.drawable.ic_male_placeholder
//
//    fun updateUserData() {
//        if (prefUtils.getUserData()?.firstname != null) {
//            _userName.value = prefUtils.getUserData()?.firstname
//            _user_profile_image.value = prefUtils.getUserData()?.profileimage
//            _standardname.value = prefUtils.getUserData()?.standardname
//        }
//    }
}