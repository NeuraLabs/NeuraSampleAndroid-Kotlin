package com.neura.sampleapplication.presentation.views

import android.content.Context
import android.view.View
import androidx.annotation.StringRes

abstract class BaseViewMvc : ViewMvc {

    lateinit var mRootView: View

    override fun getRootView(): View {
        return mRootView
    }

    protected fun setRootView(rootView: View) {
        mRootView = rootView
    }

    protected fun <T : View> findViewById(id: Int): T {
        return getRootView().findViewById(id)
    }

    protected fun getContext(): Context? {
        return getRootView().context
    }

    protected fun getString(@StringRes id: Int): String? {
        return getContext()?.getString(id)
    }
}