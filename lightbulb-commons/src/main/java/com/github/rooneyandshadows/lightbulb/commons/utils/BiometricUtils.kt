package com.github.rooneyandshadows.lightbulb.commons.utils

import android.app.admin.DevicePolicyManager
import android.content.Context
import android.content.Intent
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.*
import androidx.biometric.BiometricManager.Authenticators.*
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

@Suppress("unused", "UnnecessaryVariable")
class BiometricUtils {
    companion object {

        @JvmStatic
        fun canAuthenticateWithBiometry(context: Context): Boolean {
            val manager = from(context)
            return when (manager.canAuthenticate(BIOMETRIC_STRONG)) {
                BIOMETRIC_SUCCESS -> true
                BIOMETRIC_ERROR_NO_HARDWARE -> false //"No biometric features available on this device."
                BIOMETRIC_ERROR_HW_UNAVAILABLE -> false //"Biometric features are currently unavailable."
                BIOMETRIC_ERROR_NONE_ENROLLED -> false //"No finger prints found"
                else -> false //"Unknown cause"
            }
        }



        @JvmStatic
        fun requestBiometricAuthentication(
            fragment: Fragment,
            settings: AuthSettings
        ): Companion {
            val context = fragment.requireContext()
            buildPrompt(fragment, settings).apply {
                callPrompt(context, this, settings)
            }
            return Companion
        }

        @JvmStatic
        fun requestBiometricAuthentication(
            activity: FragmentActivity,
            settings: AuthSettings
        ): Companion {
            val context = activity
            buildPrompt(activity, settings).apply {
                callPrompt(context, this, settings)
            }

            return Companion
        }

        private fun buildPrompt(activity: FragmentActivity, settings: AuthSettings): BiometricPrompt {
            val context = activity
            val executor = ContextCompat.getMainExecutor(context)
            return BiometricPrompt(activity, executor, object : AuthenticationCallback() {
                @Override
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    handleAuthError(context, errorCode, errString.toString(), settings)
                }

                @Override
                override fun onAuthenticationSucceeded(result: AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    settings.authenticationListeners?.onAuthenticationSucceeded()
                }

                @Override
                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    settings.authenticationListeners?.onAuthenticationFailed()
                }
            })
        }

        private fun buildPrompt(fragment: Fragment, settings: AuthSettings): BiometricPrompt {
            val context = fragment.requireContext()
            val executor = ContextCompat.getMainExecutor(context)
            return BiometricPrompt(fragment, executor, object : AuthenticationCallback() {
                @Override
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    handleAuthError(context, errorCode, errString.toString(), settings)
                }

                @Override
                override fun onAuthenticationSucceeded(result: AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    settings.authenticationListeners?.onAuthenticationSucceeded()
                }

                @Override
                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    settings.authenticationListeners?.onAuthenticationFailed()
                }
            })
        }

        private fun handleAuthError(context: Context, errorCode: Int, errorString: String, authSettings: AuthSettings) {
            val noBiometrics = errorCode == ERROR_NO_BIOMETRICS
            val noPinOrFigure = errorCode == ERROR_NO_DEVICE_CREDENTIAL
            if ((noBiometrics || noPinOrFigure) && authSettings.forceCreateAuthIfNone)
                requestCreateCredentials(context)
            else
                authSettings.authenticationListeners?.onAuthenticationError(errorCode, errorString)
        }

        private fun callPrompt(
            context: Context,
            biometricPrompt: BiometricPrompt,
            settings: AuthSettings
        ) {
            val manager = from(context)
            when (manager.canAuthenticate(BIOMETRIC_WEAK or BIOMETRIC_STRONG)) {
                BIOMETRIC_SUCCESS -> {
                    //"App can authenticate using biometrics."
                    requestFingerPrint(
                        biometricPrompt,
                        settings
                    )
                }
                BIOMETRIC_ERROR_NO_HARDWARE -> {
                    //"No biometric features available on this device."
                    if (!settings.allowPinOrFigure) {
                        settings.authenticationListeners?.onAuthenticationError(
                            BIOMETRIC_ERROR_NO_HARDWARE,
                            "No biometric features available on this device."
                        )
                        return
                    }
                    requestPinOrFigure(
                        biometricPrompt,
                        settings
                    )
                }
                BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                    //"Biometric features are currently unavailable."
                    if (!settings.allowPinOrFigure) {
                        settings.authenticationListeners?.onAuthenticationError(
                            BIOMETRIC_ERROR_HW_UNAVAILABLE,
                            "Biometric features are currently unavailable."
                        )
                        return
                    }
                    requestPinOrFigure(
                        biometricPrompt,
                        settings
                    )
                }
                BIOMETRIC_ERROR_NONE_ENROLLED -> {
                    //"No finger prints found"
                    if (!settings.allowPinOrFigure) {
                        settings.authenticationListeners?.onAuthenticationError(
                            BIOMETRIC_ERROR_NONE_ENROLLED,
                            "No finger prints found"
                        )
                        return
                    }
                    requestPinOrFigure(
                        biometricPrompt,
                        settings
                    )
                }
                else -> {
                    //"Unknown cause"
                }
            }
        }

        private fun requestFingerPrint(
            biometricPrompt: BiometricPrompt,
            settings: AuthSettings
        ) {
            val dialog = PromptInfo.Builder()
                .setTitle(settings.title)
                .setSubtitle(settings.subTitle)
            val promptInfo: PromptInfo.Builder = dialog
            promptInfo.setNegativeButtonText(settings.cancelButtonText)
            biometricPrompt.authenticate(promptInfo.build())
        }

        private fun requestPinOrFigure(
            biometricPrompt: BiometricPrompt,
            settings: AuthSettings
        ) {
            val dialog = PromptInfo.Builder()
                .setTitle(settings.title)
                .setSubtitle(settings.subTitle)
            val promptInfo: PromptInfo.Builder = dialog
            promptInfo.setAllowedAuthenticators(BIOMETRIC_WEAK or DEVICE_CREDENTIAL)
            biometricPrompt.authenticate(promptInfo.build())
        }

        private fun requestCreateCredentials(context: Context) {
            // Prompts the user to create credentials that your app accepts.
            //Open settings to set credential
            val intent = Intent(DevicePolicyManager.ACTION_SET_NEW_PASSWORD)
            context.startActivity(intent)
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                // Prompts the user to create credentials that your app accepts.
                //Open settings to set credential
                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL)
                enrollIntent.putExtra(
                    Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                    BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL
                )
                context.startActivity(enrollIntent)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val intent = Intent(Settings.ACTION_FINGERPRINT_ENROLL)
                context.startActivity(intent)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val intent = Intent(Settings.ACTION_SECURITY_SETTINGS)
                context.startActivity(intent)
            }*/
        }
    }

    class AuthSettings {
        var allowPinOrFigure: Boolean = false
            private set
        var title: String = "Biometric Auth"
            private set
        var subTitle: String = "Please authenticate using your biometric credential"
            private set
        var cancelButtonText: String = "Cancel"
            private set
        var forceCreateAuthIfNone: Boolean = false
            private set
        var authenticationListeners: AuthenticationListeners? = null
            private set

        fun withPinOrFigure(allowPinOrFigure: Boolean): AuthSettings {
            return apply { this.allowPinOrFigure = allowPinOrFigure }
        }

        fun withTitle(title: String): AuthSettings {
            return apply { this.title = title }
        }

        fun withSubTitle(subTitle: String): AuthSettings {
            return apply { this.subTitle = subTitle }
        }

        fun withCancelButtonText(cancelButtonText: String): AuthSettings {
            return apply { this.cancelButtonText = cancelButtonText }
        }

        fun withForceCreateAuthIfNone(forceCreateAuthIfNone: Boolean): AuthSettings {
            return apply { this.forceCreateAuthIfNone = forceCreateAuthIfNone }
        }

        fun withListeners(authenticationListeners: AuthenticationListeners?): AuthSettings {
            return apply { this.authenticationListeners = authenticationListeners }
        }

        open class AuthenticationListeners {
            open fun onAuthenticationError(errorCode: Int, errString: String) {}

            open fun onAuthenticationSucceeded() {}

            open fun onAuthenticationFailed() {}
        }
    }
}