package com.neura.sampleapplication.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.Nullable
import com.neura.sampleapplication.presentation.screens.authentication.AuthenticationViewMvc
import com.neura.sampleapplication.presentation.screens.authentication.AuthenticationViewMvcImpl

class ViewMvcFactory(inflater : LayoutInflater) {

    private val mLayoutInflater = inflater

    fun getAuthenticationViewMvc(@Nullable parent: ViewGroup?) : AuthenticationViewMvc {
        return AuthenticationViewMvcImpl(mLayoutInflater, parent)
    }
}