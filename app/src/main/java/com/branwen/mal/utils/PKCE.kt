package com.branwen.mal.utils

import android.util.Base64
import java.security.SecureRandom

object PKCE {
    var codeVerifier: String? = null

    fun generateCodeVerifier(): Unit {
        val random = SecureRandom()
        val bytes = ByteArray(43)
        random.nextBytes(bytes)
        codeVerifier = Base64.encodeToString(
            bytes,
            Base64.URL_SAFE
                    or Base64.NO_PADDING
                    or Base64.NO_WRAP
        )
    }

    fun generateCodeChallenge(verifier: String): String? {
        return codeVerifier
    }
}