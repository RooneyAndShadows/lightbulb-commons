package com.github.rooneyandshadows.lightbulb.commons.utils

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class GoogleSignInUtils {
    companion object {
        @JvmStatic
        fun initializeGoogleAccountPicker(
            fragment: Fragment,
            callbacks: GoogleSignInCallbacks
        ): ActivityResultLauncher<Intent> {
            return fragment.registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { result ->
                val task =
                    GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    callbacks.onSuccess(account)
                } catch (e: ApiException) {
                    callbacks.onError(e)
                }
            }
        }

        @JvmStatic
        fun initializeGoogleAccountPicker(
            activity: AppCompatActivity,
            callbacks: GoogleSignInCallbacks
        ): ActivityResultLauncher<Intent> {
            return activity.registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { result ->
                val task =
                    GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    callbacks.onSuccess(account)
                } catch (e: ApiException) {
                    callbacks.onError(e)
                }
            }
        }

        @JvmStatic
        fun openGoogleAccountPicker(
            context: Context,
            picker: ActivityResultLauncher<Intent>,
            googleServerAuthCode: String
        ) {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestServerAuthCode(googleServerAuthCode)
                .requestEmail()
                .build()
            val mGoogleSignInClient = GoogleSignIn.getClient(context, gso)
            val signInIntent = mGoogleSignInClient.signInIntent
            picker.launch(signInIntent)
        }

        @JvmStatic
        fun openGoogleAccountPicker(
            activity: AppCompatActivity,
            picker: ActivityResultLauncher<Intent>,
            googleServerAuthCode: String
        ) {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestServerAuthCode(googleServerAuthCode)
                .requestEmail()
                .build()
            val mGoogleSignInClient = GoogleSignIn.getClient(activity, gso)
            val signInIntent = mGoogleSignInClient.signInIntent
            picker.launch(signInIntent)
        }

        @JvmStatic
        fun openGoogleAccountPicker(
            fragment: Fragment,
            picker: ActivityResultLauncher<Intent>,
            googleServerAuthCode: String
        ) {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestServerAuthCode(googleServerAuthCode)
                .requestEmail()
                .build()
            val mGoogleSignInClient = GoogleSignIn.getClient(fragment.requireContext(), gso)
            val signInIntent = mGoogleSignInClient.signInIntent
            picker.launch(signInIntent)
        }

        interface GoogleSignInCallbacks {
            fun onSuccess(account: GoogleSignInAccount)

            fun onError(error: Exception)
        }
    }
}