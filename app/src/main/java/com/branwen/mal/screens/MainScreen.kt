package com.branwen.mal.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
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

    val currentDestination = navController.currentBackStackEntry?.destination?.route

    val showBackButton = currentDestination?.startsWith("anime_details") == true

    BranwenMALTheme {
        Scaffold(
            bottomBar = {
                BottomNavigationBar(navController)
            },
            topBar = {
                TopAppBar(
                    colors = TopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        titleContentColor = MaterialTheme.colorScheme.onSurface,
                        scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                        actionIconContentColor = MaterialTheme.colorScheme.onSurface,
                    ),
                    title = {
                        Text(text = "MAL")
                    },
                    navigationIcon = {
                        if (showBackButton) {
                            IconButton(
                                onClick = { navController.popBackStack() }
                            ) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                            }
                        }
                    }
                )
            }
        ) { innerPadding ->
            NavHost(
                modifier = Modifier.padding(innerPadding),
                navController = navController,
                startDestination = "home"
            ) {
                composable("home") { HomeScreen() }
                composable("discover") { DiscoverScreen(navController) }
                composable("mylist") { MyListScreen() }
                composable(
                    "anime_details/{animeId}",
                    arguments = listOf(navArgument("animeId") { type = NavType.IntType })
                ) { backStackEntry ->
                    AnimeDetailsScreen(navController)
                }
            }
        }
    }
}