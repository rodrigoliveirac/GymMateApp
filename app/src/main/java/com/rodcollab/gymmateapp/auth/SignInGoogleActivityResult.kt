package com.rodcollab.gymmateapp.auth

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.appcheck.internal.util.Logger

class SignInGoogleActivityResult : ActivityResultContract<Intent, String?>() {
    override fun createIntent(context: Context, input: Intent) = input

    override fun parseResult(resultCode: Int, intent: Intent?) : String? {
        val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
        return try {
            val account = task. getResult(ApiException::class.java)!!
            Log.d(Logger.TAG, "firebaseAuthWithGoogle:" + account.id)
            account.idToken!!
        } catch (e: ApiException) {
            Log.w(Logger.TAG, "Google sign in failed", e)
            null
        }
    }
}