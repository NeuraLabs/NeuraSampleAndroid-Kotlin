package com.neura.sampleapplication.presentation.screens.authentication

import com.neura.sampleapplication.presentation.views.ViewMvc

interface AuthenticationViewMvc : ViewMvc {
    fun connect()
    fun disconnect()
}