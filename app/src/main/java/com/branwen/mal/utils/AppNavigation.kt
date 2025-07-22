package com.branwen.mal.utils

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.branwen.mal.screens.LoginScreen
import com.branwen.mal.screens.MainScreen
import com.branwen.mal.screens.SplashScreen

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