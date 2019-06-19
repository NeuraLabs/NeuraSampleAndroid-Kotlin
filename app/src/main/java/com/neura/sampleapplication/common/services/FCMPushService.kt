package com.neura.sampleapplication.common.services

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.neura.sampleapplication.SampleApplication
import com.neura.standalonesdk.events.NeuraEvent
import com.neura.standalonesdk.events.NeuraEventCallBack
import com.neura.standalonesdk.events.NeuraPushCommandFactory

class FCMPushService : FirebaseMessagingService() {

    override fun onNewToken(newToken: String?) {
        super.onNewToken(newToken)
        if (newToken != null) {
            (application as SampleApplication).mCompositionRoot.mNeuraHelper.registerNewFCMToken(newToken)
        }
    }

    override fun onMessageReceived(message: RemoteMessage?) {
        /* It is important to call isNeuraPush even if you are not register to ant event,
         Neura Authentication flow is dependant on authentication push. */
        val isNeuraPush = NeuraPushCommandFactory.getInstance()
            .isNeuraPush(applicationContext, message?.data, object : NeuraEventCallBack {
                override fun neuraEventDetected(event: NeuraEvent?) {
                    val eventText = event?.toString() ?: "couldn't parse data"
                    Log.i(javaClass.simpleName, "received Neura event - $eventText")
                }
            })

        if (!isNeuraPush) {
            //Handle non neura push here
        }

    }

}