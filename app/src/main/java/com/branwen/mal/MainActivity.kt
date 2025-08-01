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
import com.branwen.mal.ui.theme.BranwenMALTheme
import com.branwen.mal.utils.AppNavigation
import com.branwen.mal.utils.PKCE
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
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

    fun provideAuthService(): MalAuthService {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = retrofit2.Retrofit.Builder()
            .baseUrl("https://myanimelist.net")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        return retrofit.create(MalAuthService::class.java)
    }
}

interface MalAuthService {
    @FormUrlEncoded
    @POST("/v1/oauth2/token")
    suspend fun exchangeToken(
        @Field("grant_type") grantType: String = "authorization_code",
        @Field("client_id") clientId: String,
        @Field("code") code: String,
        @Field("code_verifier") codeVerifier: String,
    ): TokenResponse
}

data class TokenResponse(
    @Json(name = "expires_in") val expiresIn: Int,
    @Json(name = "access_token") val accessToken: String,
    @Json(name = "refresh_token") val refreshToken: String
)