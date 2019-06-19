package com.neura.sampleapplication.helpers.common

import android.app.Activity
import android.content.Context

interface PermissionHelper {

    fun isPermissionGranted(context: Context, permissions: Array<String>): Boolean

    fun requestPermission(activity: Activity, permissions: Array<String>, requestCode: Int)
}