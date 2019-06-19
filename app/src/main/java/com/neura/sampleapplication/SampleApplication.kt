package com.neura.sampleapplication

import android.app.Application
import com.neura.sampleapplication.dependencyinjection.CompositionRoot

class SampleApplication : Application() {

    lateinit var mCompositionRoot : CompositionRoot

    override fun onCreate() {
        super.onCreate()
        mCompositionRoot = CompositionRoot(this)
    }
}