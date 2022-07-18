Biometric Authentication Dialog [Face Id and Touch Id]

Usage

1. Gradle dependencies:

- Add below code in your app level "build.gradle" file.

`dependencies {
    implementation("androidx.biometric:biometric:1.2.0-alpha04")
}`


2. Biometric Dialog

- Now add below code for open biometric dialog


```
private lateinit var executor: Executor
private lateinit var promptInfo: BiometricPrompt.PromptInfo

executor = ContextCompat.getMainExecutor(this)
 
promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Login") // set title of pop-up
            .setSubtitle("Log in using your biometric credential") // set subtitle of pop-up
            .setConfirmationRequired(true) // set confirmation button true/false after verified the face id and fingerprint
            .setNegativeButtonText("Cancel") // set negative button label
            .build()

```

			
- Now  write a click listener and open biometric dialog


```
biometricLoginButton.setOnClickListener {
            // check device is supported biometric    
            if (checkDeviceIsSupportedBiometric()) {
                biometricPrompt.authenticate(promptInfo)
            }
}
```
		
     
3. Handling results

Handle authentication result 

```
biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                // get error response here if user face id and touch id getting invalid
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)

                    Log.e("onAuthenticationError",  "Error Code: $errorCode, Error Message: $errString")
              
						Toast.makeText(applicationContext, "Authentication error: $errString", Toast.LENGTH_SHORT).show()    
                }

                // get success response here after user face id or touch id successful verified
                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
					Toast.makeText(applicationContext, "Authentication succeeded!", Toast.LENGTH_SHORT).show()              
                }

                // get authentication failed when user denied the biometric authentication prompt
                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(applicationContext, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            })
```

4. If we want to check if the user device is supported fingerprint or face id then check with below method

```
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
```

