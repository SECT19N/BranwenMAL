package com.branwen.mal.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.branwen.mal.presentation.ui.login.LoginScreen
import com.branwen.mal.presentation.ui.main.MainScreen
import com.branwen.mal.presentation.ui.splash.SplashScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(navController)
        }
        composable("login") {
            LoginScreen(navController)
        }
        composable("main") {
            MainScreen()
        }
    }
}