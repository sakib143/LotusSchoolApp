package com.appforschool.ui.home

import androidx.lifecycle.AndroidViewModel
import com.appforschool.MyApp
import com.appforschool.utils.GlobalMethods
import com.appforschool.utils.PrefUtils
import javax.inject.Inject

class HomeActivitViewModel  @Inject constructor(
    private val application: MyApp,
    private val prefUtils: PrefUtils,
    private val globalMethods: GlobalMethods
) : AndroidViewModel(application)  {


}