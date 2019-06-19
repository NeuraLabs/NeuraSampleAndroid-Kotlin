package com.neura.sampleapplication.presentation.controllers.activities

import androidx.appcompat.app.AppCompatActivity
import com.neura.sampleapplication.SampleApplication
import com.neura.sampleapplication.dependencyinjection.ControllerCompositionRoot
import com.neura.sampleapplication.helpers.common.PermissionHelperImpl

abstract class BaseActivity : AppCompatActivity() {

    private var mControllerCompositionRoot: ControllerCompositionRoot? = null

    fun getCompositionRoot(): ControllerCompositionRoot {
        if (mControllerCompositionRoot == null) {
            mControllerCompositionRoot =
                ControllerCompositionRoot((application as SampleApplication).mCompositionRoot, this)
        }
        return mControllerCompositionRoot!!
    }

    fun requestPermission(permissions: Array<String>, requestCode: Int) {
        val permissionHelper = PermissionHelperImpl()
        if (!permissionHelper.isPermissionGranted(this, permissions)) {
            permissionHelper.requestPermission(this, permissions, requestCode)
        }
    }
}