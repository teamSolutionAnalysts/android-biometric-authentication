package com.biometric_auth

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import java.util.concurrent.Executor

class MainActivity : AppCompatActivity() {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        executor = ContextCompat.getMainExecutor(this)

        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                // get error response here if user face id and touch id getting invalid
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)

                    Log.e(
                        "onAuthenticationError",
                        "Error Code: $errorCode, Error Message: $errString"
                    )
                    Toast.makeText(
                        applicationContext,
                        "Authentication error: $errString", Toast.LENGTH_SHORT
                    )
                        .show()
                }

                // get success response here after user face id or touch id successful verified
                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(
                        applicationContext,
                        "Authentication succeeded!", Toast.LENGTH_SHORT
                    )
                        .show()
                }

                // get authentication failed when user denied the biometric authentication prompt
                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(
                        applicationContext, "Authentication failed",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            })


        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Login") // set title of pop-up
            .setSubtitle("Log in using your biometric credential") // set subtitle of pop-up
            .setConfirmationRequired(true) // set confirmation button true/false after verified the face id and fingerprint
            .setNegativeButtonText("Cancel") // set negative button label
            .build()

        // Prompt appears when user clicks "Authenticate".
        val biometricLoginButton =
            findViewById<Button>(R.id.biometric_login)

        biometricLoginButton.setOnClickListener {
            // check device is supported biometric
            if (checkDeviceIsSupportedBiometric()) {
                biometricPrompt.authenticate(promptInfo)
            }
        }
    }


    private fun checkDeviceIsSupportedBiometric(): Boolean {

        return if (packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
            true
        } else if (packageManager.hasSystemFeature(PackageManager.FEATURE_IRIS)) {
            true
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            packageManager.hasSystemFeature(PackageManager.FEATURE_FACE)
        } else {
            return false
        }

    }
}