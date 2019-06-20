package com.neura.sampleapplication.presentation.views

interface ObservableViewMVC<ListenerType> : ViewMvc {

    fun registerListener(listener: ListenerType)
    fun unRegisterListeners(listener: ListenerType)
}