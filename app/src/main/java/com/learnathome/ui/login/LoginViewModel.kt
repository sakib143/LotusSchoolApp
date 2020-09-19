package com.learnathome.ui.login

import androidx.lifecycle.AndroidViewModel
import com.learnathome.MyApp
import javax.inject.Inject

class LoginViewModel  @Inject constructor(
    private val application: MyApp
) : AndroidViewModel(application) {

}