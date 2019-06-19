package com.neura.sampleapplication.presentation.screens.authentication

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.neura.resources.user.UserDetails
import com.neura.resources.user.UserDetailsCallbacks
import com.neura.sampleapplication.R
import com.neura.sampleapplication.common.utils.Utils
import com.neura.sampleapplication.neura.NeuraHelper
import com.neura.sampleapplication.presentation.views.BaseViewMvc

class AuthenticationViewMvcImpl(inflater: LayoutInflater, @Nullable parent: ViewGroup?, neuraHelper: NeuraHelper) :
    BaseViewMvc(),
    AuthenticationViewMvc, View.OnClickListener, UserDetailsCallbacks, NeuraHelper.AuthListener {

    companion object {
        const val TAG = "AuthenticationScreen"
    }

    private val mConnectBtn: Button
    private val mDisconnectBtn: Button
    private val mProgressBar: ProgressBar
    private val mNeuraStatusTv: TextView
    private val mNeuraUserIdTv: TextView
    private val mNeuraVersion: TextView
    private val mNeuraUserId: TextView
    private val mUpperNeuraSymbol: AppCompatImageView
    private val mBottomNeuraSymbol: AppCompatImageView
    private val mNeuraHelper = neuraHelper

    init {
        setRootView(inflater.inflate(R.layout.authentication_screen, parent, false))
        mConnectBtn = findViewById(R.id.connect_btn)
        mDisconnectBtn = findViewById(R.id.disconnect_btn)
        mProgressBar = findViewById(R.id.pb)
        mNeuraStatusTv = findViewById(R.id.neura_status)
        mNeuraUserIdTv = findViewById(R.id.neura_user_id)
        mUpperNeuraSymbol = findViewById(R.id.upper_icon)
        mBottomNeuraSymbol = findViewById(R.id.bottom_icon)
        mNeuraUserId = findViewById(R.id.neura_user_id)
        mNeuraVersion = findViewById(R.id.neura_version)
        setClickListeners()
        setVersion()
        setScreenState()
    }

    /**
     * Will set the screen state according to authentication state of Neura.
     */
    private fun setScreenState() {
        if (getContext() != null) {
            if (mNeuraHelper.isLoggedIn()) {
                mConnectBtn.visibility = View.GONE
                mDisconnectBtn.isEnabled = true
                mBottomNeuraSymbol.setPadding(0, 0, 0, 0)
                mUpperNeuraSymbol.setPadding(0, 0, 0, 0)
                getUserDetails()
                mNeuraStatusTv.text = getString(R.string.neura_status_connected)
                mNeuraStatusTv.setTextColor(ContextCompat.getColor(getContext()!!, R.color.green_connected))

            } else {
                mConnectBtn.visibility = View.VISIBLE
                mDisconnectBtn.isEnabled = false
                val padding: Int = (Utils().getScreenDensity(getContext()) * 60).toInt()
                mBottomNeuraSymbol.setPadding(0, 0, padding, 0)
                mUpperNeuraSymbol.setPadding(padding, 0, 0, 0)
                mNeuraUserId.text = ""
                mNeuraStatusTv.text = getString(R.string.neura_status_disconnected)
                mNeuraStatusTv.setTextColor(ContextCompat.getColor(getContext()!!, R.color.red_disconnected))

            }
        }
    }

    private fun setVersion() {
        val versionString = "${getString(R.string.sdk_version)} ${mNeuraHelper.getNeuraVersion()}"
        mNeuraVersion.text = versionString
    }

    private fun setClickListeners() {
        mConnectBtn.setOnClickListener(this)
        mDisconnectBtn.setOnClickListener(this)
    }

    private fun getUserDetails() {
        mNeuraHelper.getUserDetails(this)
    }

    override fun onSuccess(userDetails: UserDetails?) {
        val userID = "${getString(R.string.neura_id)} ${userDetails?.data?.neuraId ?: "Unknown"}"
        mNeuraUserId.text = userID
    }

    override fun onFailure(resultData: Bundle?, errorCode: Int) {
        Log.e(TAG, "Get user Details failed with the following error code: $errorCode")
        mNeuraUserId.text = ""
    }

    /**
     * Authenticate to Neura
     */
    override fun connect() {
        mProgressBar.visibility = View.VISIBLE
        mNeuraHelper.authenticate(this)
    }


    /**
     * Disconnect from Neura
     */
    override fun disconnect() {
        mProgressBar.visibility = View.VISIBLE
        mNeuraHelper.disconnect(Handler.Callback { msg ->
            if (msg.arg1 == 1) {
                // Logged out successfully
                setScreenState()
            }
            mProgressBar.visibility = View.GONE
            true
        })

    }

    override fun authenticationCompleted() {
        mProgressBar.visibility = View.GONE
        setScreenState()
        // If you like to start Neura in foreground mode, this is a good place to place it.
        mNeuraHelper.startNeuraForeground()
    }

    override fun authenticationFailed(reason: String) {
        mProgressBar.visibility = View.GONE
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.connect_btn -> connect()
            R.id.disconnect_btn -> disconnect()
        }
    }
}