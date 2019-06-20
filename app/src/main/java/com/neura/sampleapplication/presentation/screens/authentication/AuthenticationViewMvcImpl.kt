package com.neura.sampleapplication.presentation.screens.authentication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.neura.sampleapplication.R
import com.neura.sampleapplication.common.utils.Utils
import com.neura.sampleapplication.presentation.views.BaseObservableViewMvc

class AuthenticationViewMvcImpl(inflater: LayoutInflater, @Nullable parent: ViewGroup?) :
    BaseObservableViewMvc<AuthenticationViewMvc.Listener>(),
    AuthenticationViewMvc, View.OnClickListener {

    private val mConnectBtn: Button
    private val mDisconnectBtn: Button
    private val mProgressBar: ProgressBar
    private val mNeuraStatusTv: TextView
    private val mNeuraUserIdTv: TextView
    private val mNeuraVersion: TextView
    private val mNeuraUserId: TextView
    private val mUpperNeuraSymbol: AppCompatImageView
    private val mBottomNeuraSymbol: AppCompatImageView

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
    }

    /**
     * Will set the screen state according to authentication state of Neura.
     */
    override fun setScreenState(isLoggedIn: Boolean) {
        if (getContext() != null) {
            if (isLoggedIn) {
                mConnectBtn.visibility = View.GONE
                mDisconnectBtn.isEnabled = true
                mBottomNeuraSymbol.setPadding(0, 0, 0, 0)
                mUpperNeuraSymbol.setPadding(0, 0, 0, 0)
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

    override fun setVersion(version: String) {
        mNeuraVersion.text = version
    }

    private fun setClickListeners() {
        mConnectBtn.setOnClickListener(this)
        mDisconnectBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.connect_btn -> {
                for (l: AuthenticationViewMvc.Listener in mListeners) {
                    l.connectClicked()
                }
            }
            R.id.disconnect_btn -> {
                for (l: AuthenticationViewMvc.Listener in mListeners) {
                    l.disconnectClicked()
                }
            }
        }
    }

    override fun removeProgressBar() {
        mProgressBar.visibility = View.GONE
    }

    override fun showProgressBar() {
        mProgressBar.visibility = View.VISIBLE
    }

    override fun setUserID(neuraUserID: String) {
        mNeuraUserId.text = neuraUserID
    }
}