package com.neura.sampleapplication.presentation.screens.authentication

import android.Manifest.permission.*
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.neura.resources.user.UserDetails
import com.neura.resources.user.UserDetailsCallbacks
import com.neura.sampleapplication.R

import com.neura.sampleapplication.common.helpers.PermissionHelperImpl
import com.neura.sampleapplication.neura.NeuraHelper
import com.neura.sampleapplication.presentation.controllers.activities.BaseActivity

class AuthenticationActivity : BaseActivity(), AuthenticationViewMvc.Listener, NeuraHelper.AuthListener,
    UserDetailsCallbacks {

    companion object {
        const val LOC_PERMISSION_REQUEST_CODE = 111
        const val ACTIVITY_RECOGNITION_REQUEST_CODE = 222
        const val TAG = "AuthenticationActivity"
    }

    lateinit var mViewMvc: AuthenticationViewMvc

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestLocPermission()
        mViewMvc = getCompositionRoot().getViewMvcFactory().getAuthenticationViewMvc(null)
        initMvcView()
        setContentView(mViewMvc.getRootView())
    }

    private fun initMvcView() {
        val neuraHelper = getCompositionRoot().getNeuraHelper()
        mViewMvc.setScreenState(getCompositionRoot().getNeuraHelper().isLoggedIn())
        if (isLoggedIn()) neuraHelper.getUserDetails(this)
        mViewMvc.setVersion("${getString(R.string.sdk_version)} ${neuraHelper.getNeuraVersion()}")
        mViewMvc.registerListener(this)
    }

    override fun connectClicked() {
        mViewMvc.showProgressBar()
        getCompositionRoot().getNeuraHelper().authenticate(this)
    }

    override fun disconnectClicked() {
        mViewMvc.showProgressBar()
        getCompositionRoot().getNeuraHelper().disconnect(Handler.Callback { msg ->
            if (msg.arg1 == 1) {
                // Logged out successfully
                mViewMvc.setScreenState(isLoggedIn())
            }
            mViewMvc.removeProgressBar()
            true
        })
    }

    override fun authenticationCompleted() {
        mViewMvc.setScreenState(isLoggedIn())
        mViewMvc.removeProgressBar()
        getCompositionRoot().getNeuraHelper().getUserDetails(this)
    }

    private fun requestLocPermission(){
        val hasForegroundPermission = PermissionHelperImpl().isPermissionGranted(this, arrayOf(ACCESS_FINE_LOCATION))
        if(hasForegroundPermission){
            val hasBackgroundPermission = PermissionHelperImpl().isPermissionGranted(this, arrayOf(ACCESS_BACKGROUND_LOCATION))
            if(!hasBackgroundPermission){
                requestPermission(arrayOf(ACCESS_BACKGROUND_LOCATION), LOC_PERMISSION_REQUEST_CODE)
            }
        } else{
            requestPermission(arrayOf(ACCESS_FINE_LOCATION, ACCESS_BACKGROUND_LOCATION), LOC_PERMISSION_REQUEST_CODE)
        }
    }

    override fun authenticationFailed(reason: String) {
        mViewMvc.removeProgressBar()
    }

    private fun isLoggedIn(): Boolean {
        return getCompositionRoot().getNeuraHelper().isLoggedIn()
    }

    override fun onSuccess(userDetails: UserDetails?) {
        mViewMvc.setUserID("${getString(R.string.neura_id)} ${userDetails?.data?.neuraId ?: "Unknown"}")
    }

    override fun onFailure(resultData: Bundle?, errorCode: Int) {
        Log.e(TAG, "GetUserDetails failed with error code: $errorCode")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOC_PERMISSION_REQUEST_CODE) {
            // handle loc permission result if needed.
            requestPermission(arrayOf(ACTIVITY_RECOGNITION), ACTIVITY_RECOGNITION_REQUEST_CODE)
        }
    }
}
