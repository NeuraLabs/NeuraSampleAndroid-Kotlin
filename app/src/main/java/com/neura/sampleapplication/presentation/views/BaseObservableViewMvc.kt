package com.neura.sampleapplication.presentation.views

open class BaseObservableViewMvc<ListenerType> : BaseViewMvc(), ObservableViewMVC<ListenerType> {

    val mListeners = ArrayList<ListenerType>(1)

    override fun registerListener(listener: ListenerType) {
        mListeners.add(listener)
    }

    override fun unRegisterListeners(listener: ListenerType) {
        mListeners.remove(listener)
    }
}