package com.branwen.mal.screens

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.branwen.mal.components.BottomNavigationBar
import com.branwen.mal.ui.theme.BranwenMALTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    BranwenMALTheme {
        Scaffold(
            bottomBar = {
                BottomNavigationBar(navController)
            }
        ) { innerPadding ->
            NavHost(
                modifier = Modifier
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding),
                navController = navController,
                startDestination = "home"
            ) {
                composable("home") { HomeScreen() }
                composable("discover") { DiscoverScreen(navController) }
                composable("my_list") {
                    MyListScreen(
                        navigate = { animeId ->
                            navController.navigate("anime_details/$animeId")
                        }
                    )
                }
                composable(
                    "anime_details/{animeId}",
                    arguments = listOf(navArgument("animeId") { type = NavType.IntType })
                ) { backStackEntry ->
                    AnimeDetailsScreen(navController = navController, onBack = { navController.popBackStack() })
                }
            }
        }
    }
}