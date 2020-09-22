package com.appforschool.base

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.appforschool.di.Injectable

/**
 * Generic class To Use ShopOtherWayModel Binding in Activities
 */
abstract class BaseBindingActivity<B : ViewDataBinding> : BaseActivity(), Injectable {

    /**
     * Object of ViewDataBinding
     * */
    private var _binding: B? = null
    private val binding: B?
        get() = _binding

    /**
     * this method gets called when this activity gets created
     * */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inflateView(layoutId())
    }

    /**
     * Inflates Layout with
     * @param layoutResID -> inflates layout from ID
     */
    private fun inflateView(layoutResID: Int) {
        _binding = DataBindingUtil.setContentView(this, layoutResID)
        binding?.let { initializeBinding(it) }
    }

    /**
     * Used to not forget to initialize the binding
     */
    abstract fun initializeBinding(binding: B)

}