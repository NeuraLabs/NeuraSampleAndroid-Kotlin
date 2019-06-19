package com.neura.sampleapplication.neura

import android.content.Context
import android.os.Handler
import android.util.Log
import androidx.annotation.Nullable
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import com.neura.resources.authentication.AnonymousAuthenticateCallBack
import com.neura.resources.authentication.AnonymousAuthenticateData
import com.neura.resources.authentication.AnonymousAuthenticationStateListener
import com.neura.resources.authentication.AuthenticationState
import com.neura.resources.user.UserDetailsCallbacks
import com.neura.sdk.`object`.AnonymousAuthenticationRequest
import com.neura.standalonesdk.service.NeuraApiClient
import com.neura.standalonesdk.util.SDKUtils


/**
 * Helper class that Wraps [NeuraApiClient]
 */
class NeuraHelper(context: Context) : AnonymousAuthenticationStateListener {

    companion object {

        const val TAG = "NeuraHelper"

        const val APP_UID = "YOUR_NEURA_APP_ID"
        const val APP_SECRET = "YOUR_NEURA_APP_SECRET"
    }

    private var mNeuraApiClient: NeuraApiClient =
        NeuraApiClient.getClient(context.applicationContext, APP_UID, APP_SECRET)

    var mListener: AuthListener? = null

    /**
     * Starting Neura Authentication flow.
     */
    fun authenticate(@Nullable listener: AuthListener) {
        mListener = listener
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e(TAG, "FCM task completed with a Failure.")
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val pushToken = task.result?.token
                if (pushToken != null) {
                    //Instantiate AnonymousAuthenticationRequest instance.
                    val request = AnonymousAuthenticationRequest(pushToken)

                    //Pass the AnonymousAuthenticationRequest instance and register a call back for success and failure events.
                    mNeuraApiClient.authenticate(request, object : AnonymousAuthenticateCallBack {
                        override fun onSuccess(authenticateData: AnonymousAuthenticateData) {
                            mNeuraApiClient.registerAuthStateListener(this@NeuraHelper)
                            Log.i(TAG, "Successfully requested authentication with neura.")
                        }

                        override fun onFailure(errorCode: Int) {
                            val reason = SDKUtils.errorCodeToString(errorCode)
                            Log.e(TAG, "Failed to authenticate with neura. Reason : $reason")
                            mListener?.authenticationFailed(reason)
                        }
                    })
                } else {
                    Log.e(TAG, "FCM token is null, cannot proceed with Auth flow.")
                }
            })
    }

    /**
     * Indicates that the auth state has changed.
     */
    override fun onStateChanged(state: AuthenticationState?) {
        Log.i(TAG, "Neura authentication state: ${state?.name}")
        when (state) {
            AuthenticationState.AuthenticatedAnonymously -> {
                // Authenticated anonymously and can start using NeuraSDK
                mNeuraApiClient.unregisterAuthStateListener()
                mListener?.authenticationCompleted()
            }
            AuthenticationState.NotAuthenticated, AuthenticationState.FailedReceivingAccessToken -> {
                // Authentication failed indefinitely. a good opportunity to retry the authentication flow
                mNeuraApiClient.unregisterAuthStateListener()
            }
            AuthenticationState.AccessTokenRequested -> {
                // Successfully requested an Auth push from Neura backend.
            }
            AuthenticationState.Authenticated -> {
                // Authenticated and can start using NeuraSDK (deprecated state)
            }
        }
    }

    /**
     * @return True - if the user is authenticated, otherwise - False
     */
    fun isLoggedIn(): Boolean {
        return mNeuraApiClient.isLoggedIn
    }

    /**
     * Fetches UserDetails via [NeuraApiClient.getUserDetails]
     *
     * @param callback user details will be returned using this callback.
     */
    fun getUserDetails(callback: UserDetailsCallbacks) {
        mNeuraApiClient.getUserDetails(callback)
    }

    /**
     * Call it if you want to move Neura into foreground mode by calling [NeuraApiClient.startNeuraForeground]
     * Note - user should be logged in when you call this method.
     */
    fun startNeuraForeground() {
        mNeuraApiClient.startNeuraForeground(true)
    }

    /**
     * Disconnect from Neura by calling [NeuraApiClient.forgetMe]
     *
     * @paramc callBack Success or Failure will be passed via this [Handler.Callback]
     *          for success, arg1 of the returned message will have the value of 1
     */
    fun disconnect(callBack: Handler.Callback) {
        mNeuraApiClient.forgetMe(callBack)
    }

    /**
     * Notify Neura that FCM token has been changed
     *
     * @param token The new FCM token as received from [FirebaseMessagingService.onNewToken]
     */
    fun registerNewFCMToken(token: String) {
        mNeuraApiClient.registerFirebaseToken(token)
    }

    /**
     * @return a string representation of the Neura SDK verison.
     */
    fun getNeuraVersion(): String {
        return mNeuraApiClient.sdkVersion
    }

    /**
     * An interface to notify for auth success/Failure
     */
    interface AuthListener {
        fun authenticationCompleted()
        fun authenticationFailed(reason: String)
    }

}