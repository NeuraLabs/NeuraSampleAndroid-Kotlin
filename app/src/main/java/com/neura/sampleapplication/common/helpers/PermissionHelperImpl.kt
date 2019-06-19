package com.neura.sampleapplication.common.helpers

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

class PermissionHelperImpl : PermissionHelper {

    /**
     * check if the passed permissions are granted.
     * @return True only if ALL passed permissions are granted, otherwise - False.
     */
    override fun isPermissionGranted(context: Context, permissions: Array<String>): Boolean {

        for (permission in permissions) {
            val granted = ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
            if (!granted) {
                return false
            }
        }
        return true
    }

    /**
     * Will request permission for the given permissions.
     */
    override fun requestPermission(activity: Activity, permissions: Array<String>, requestCode: Int) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode)
    }
}