package com.neura.sampleapplication.presentation.screens.authentication

import com.neura.sampleapplication.presentation.views.ObservableViewMVC

interface AuthenticationViewMvc : ObservableViewMVC<AuthenticationViewMvc.Listener> {
    fun setScreenState(isLoggedIn: Boolean)
    fun showProgressBar()
    fun removeProgressBar()
    fun setUserID(neuraUserID: String)
    fun setVersion(version: String)

    interface Listener {
        fun connectClicked()
        fun disconnectClicked()
    }
}