package com.branwen.mal.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Sealed class representing the different screens in the app.
 * Each screen has a unique route, title, and icon.
 *
 * @property route The route of the screen.
 * @property title The title of the screen.
 * @property icon The icon of the screen.
 *
 * @param route The route of the screen.
 * @param title The title of the screen.
 * @param icon The icon of the screen.
 *
 */
sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Search : Screen("discover", "Discover", Icons.Default.Search)
    object MyList : Screen("my_list", "My List", Icons.AutoMirrored.Filled.List)
}
