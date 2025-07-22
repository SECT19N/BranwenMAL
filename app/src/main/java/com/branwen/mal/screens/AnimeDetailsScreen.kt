package com.branwen.mal.screens

import android.app.Application
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.branwen.mal.viewmodels.AnimeDetailsViewModel
import com.branwen.mal.viewmodels.factory.AnimeDetailsViewModelFactory

@Composable
fun AnimeDetailsScreen(
    navController: NavController,
) {
    val context = LocalContext.current
    val application = context.applicationContext as Application

    val backStackEntry = remember { navController.currentBackStackEntry }
    val animeId = backStackEntry?.arguments?.getInt("animeId") ?: 1

    val factory = remember { AnimeDetailsViewModelFactory(application, animeId) }
    val viewModel: AnimeDetailsViewModel = viewModel(factory = factory)

    val anime = viewModel.animeDetails.value

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Card(
            modifier = Modifier
                .width(360.dp)
                .height(240.dp)
        ) {
            AsyncImage(
                model = anime?.mainPicture?.large,
                contentDescription = "Anime poster"
            )

            Text(
                text = anime?.title ?: "N/A"
            )
        }

        HorizontalDivider()

        Text(
            text = "Synopsis",
            modifier = Modifier.padding(bottom = 8.dp),
            style = MaterialTheme.typography.titleMedium,
        )

        Text(
            text = anime?.synopsis ?: "N/A"
        )
    }
}