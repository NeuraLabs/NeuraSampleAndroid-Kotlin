package com.neura.sampleapplication.dependencyinjection

import android.app.Activity
import android.view.LayoutInflater
import com.neura.sampleapplication.neura.NeuraHelper
import com.neura.sampleapplication.presentation.ViewMvcFactory

class ControllerCompositionRoot(compositionRoot: CompositionRoot, activity: Activity) {

    private val mCompositionRoot = compositionRoot
    private val mActivity = activity

    private fun getLayoutInflater(): LayoutInflater {
        return LayoutInflater.from(mActivity)
    }

    fun getNeuraHelper(): NeuraHelper {
        return mCompositionRoot.mNeuraHelper
    }

    fun getViewMvcFactory(): ViewMvcFactory {
        return ViewMvcFactory(getLayoutInflater())
    }
}