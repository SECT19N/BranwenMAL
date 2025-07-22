package com.branwen.mal.screens

import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.navigation.NavController
import com.branwen.mal.BuildConfig
import com.branwen.mal.utils.PKCE

@Composable
fun LoginScreen(
    navController: NavController
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val context = LocalContext.current
        val customTabIntent = CustomTabsIntent.Builder().build()

        Button(
            onClick = {
                val authUrl = buildAuthUrl()
                customTabIntent.launchUrl(context, authUrl.toUri())
            }
        ) {
            Text("Login with MAL")
        }
    }
}

fun buildAuthUrl(): String {
    val clientId = BuildConfig.clientId
    PKCE.generateCodeVerifier()
    Log.d("BranwenMAL", "2, ${PKCE.codeVerifier}")
    val codeChallenge = PKCE.generateCodeChallenge(PKCE.codeVerifier!!)
    return "https://myanimelist.net/v1/oauth2/authorize" +
            "?response_type=code" +
            "&client_id=$clientId" +
            "&code_challenge=$codeChallenge"
}