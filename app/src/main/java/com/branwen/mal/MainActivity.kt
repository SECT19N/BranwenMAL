package com.branwen.mal

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.branwen.mal.data.remote.api.MalServiceBuilder.provideAuthService
import com.branwen.mal.presentation.navigation.AppNavigation
import com.branwen.mal.presentation.ui.PKCE
import com.branwen.mal.presentation.ui.theme.BranwenMALTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BranwenMALTheme {
                navController = rememberNavController()
                AppNavigation(navController = navController)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        intent.data?.let { uri ->
            Timber.d("Received deep link: $uri")

            uri.getQueryParameter("code")?.let { code ->
                Timber.d("Auth code: $code")

                // Exchange code for token
                lifecycleScope.launch {
                    try {
                        val prefs = getSharedPreferences("bran_mal_prefs", MODE_PRIVATE)
                        val authService = provideAuthService()

                        val codeVerifier = PKCE.codeVerifier ?: return@launch

                        val tokenResponse = authService.exchangeToken(
                            clientId = BuildConfig.clientId,
                            code = code,
                            codeVerifier = codeVerifier
                        )

                        // Save to SharedPreferences
                        prefs.edit {
                            putString("access_token", tokenResponse.accessToken)
                                .putString("refresh_token", tokenResponse.refreshToken)
                                .putLong("expires_at", System.currentTimeMillis() + (tokenResponse.expiresIn * 1000L))
                        }

                        // Navigate to main screen
                        navController.navigate("splash") {
                            popUpTo("login") { inclusive = true }
                        }

                    } catch (e: Exception) {
                        Timber.e(e, "Token exchange failed")
                    }
                }
            }
        }
    }
}