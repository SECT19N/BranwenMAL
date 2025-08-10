package com.branwen.mal.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.branwen.mal.components.BarChart
import com.branwen.mal.components.ExpandableText
import com.branwen.mal.models.toIntMap
import com.branwen.mal.viewmodels.AnimeDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeDetailsScreen(
    viewModel: AnimeDetailsViewModel = hiltViewModel(),
    navController: NavController,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val anime by viewModel.uiModel.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text(anime?.title ?: "Details", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.shareAnime(context) }) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = "Share")
                    }
                },
                scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .aspectRatio(2f / 3f),
                shape = MaterialTheme.shapes.medium,
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                AsyncImage(
                    model = anime?.mainPicture?.large,
                    contentDescription = "Poster Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(anime?.title ?: "—", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Members: ${anime?.numListUsers ?: "—"}", style = MaterialTheme.typography.bodyMedium)
                Text("Rank: #${anime?.rank ?: "—"}", style = MaterialTheme.typography.bodyMedium)
                Text("Popularity: #${anime?.popularity ?: "—"}", style = MaterialTheme.typography.bodyMedium)
            }

            HorizontalDivider(thickness = 1.dp, color = DividerDefaults.color)

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = listOfNotNull(
                        anime?.mediaType,
                        anime?.startSeason?.year?.toString(),
                        anime?.status,
                        "${anime?.numEpisodes ?: "?"} ep",
                        "${anime?.averageEpisodeDuration?.div(60) ?: "?"} min"
                    ).joinToString(" • "),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            FlowRow(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                anime?.genres?.forEachIndexed { i, genre ->
                    FilterChip(
                        selected = false,
                        onClick = { viewModel.openGenreLink(context, genre.id) },
                        label = { Text(genre.name) },
                        colors = FilterChipDefaults.filterChipColors(),
                        shape = MaterialTheme.shapes.small
                    )
                    if (i != anime?.genres?.lastIndex) {
                        VerticalDivider(
                            modifier = Modifier.fillMaxHeight(),
                            color = MaterialTheme.colorScheme.outlineVariant,
                            thickness = 1.dp
                        )
                    }
                }
            }

            Text(
                "Synopsis",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            ExpandableText(text = anime?.synopsis ?: "—", modifier = Modifier.padding(horizontal = 16.dp))

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = 1.dp,
                color = DividerDefaults.color
            )

            BarChart(
                data = anime?.statistics?.status?.toIntMap() ?: emptyMap(),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}