package com.neura.sampleapplication.presentation.screens.authentication

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.os.Bundle
import com.neura.sampleapplication.presentation.controllers.activities.BaseActivity

class AuthenticationActivity : BaseActivity() {

    companion object {
        const val LOC_PERMISSION_REQUEST_CODE = 111
    }

    lateinit var mViewMvc: AuthenticationViewMvc

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermission(arrayOf(ACCESS_FINE_LOCATION), LOC_PERMISSION_REQUEST_CODE)
        mViewMvc = getCompositionRoot().getViewMvcFactory()
            .getAuthenticationViewMvc(null, getCompositionRoot().getNeuraHelper())
        setContentView(mViewMvc.getRootView())
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOC_PERMISSION_REQUEST_CODE) {
            // handle loc permission result if needed.
        }
    }


}
