package com.appforschool.ui.home.fragment.drive.answer

import androidx.lifecycle.AndroidViewModel
import com.appforschool.MyApp
import com.appforschool.utils.PrefUtils
import javax.inject.Inject

class AnswerViewModel @Inject constructor(
    private val application: MyApp,
    private val prefUtils: PrefUtils,
) : AndroidViewModel(application) {


}