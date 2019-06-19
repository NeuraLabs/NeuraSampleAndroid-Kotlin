package com.neura.sampleapplication.common.helpers

import android.app.Activity
import android.content.Context

interface PermissionHelper {

    fun isPermissionGranted(context: Context, permissions: Array<String>): Boolean

    fun requestPermission(activity: Activity, permissions: Array<String>, requestCode: Int)
}