package com.learnathome.ui.home

import androidx.lifecycle.AndroidViewModel
import com.learnathome.MyApp
import com.learnathome.utils.GlobalMethods
import com.learnathome.utils.PrefUtils
import javax.inject.Inject

class HomeActivitViewModel  @Inject constructor(
    private val application: MyApp,
    private val prefUtils: PrefUtils,
    private val globalMethods: GlobalMethods
) : AndroidViewModel(application)  {


}