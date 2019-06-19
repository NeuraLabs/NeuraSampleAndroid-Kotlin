package com.neura.sampleapplication.common.utils

import android.content.Context

class Utils {

    fun getScreenDensity(context: Context?): Float {
        return context?.resources?.displayMetrics?.density ?: 1f
    }

}