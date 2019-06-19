package com.neura.sampleapplication.dependencyinjection

import android.content.Context
import com.neura.sampleapplication.neura.NeuraHelper

class CompositionRoot(context: Context) {

    var mNeuraHelper: NeuraHelper = NeuraHelper(context)

}