package com.branwen.mal.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.branwen.mal.R
import com.branwen.mal.models.AnimeListItem
import com.branwen.mal.viewmodels.DiscoverViewModel
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverScreen(
    navController: NavController,
    viewModel: DiscoverViewModel = viewModel()
) {
    val topTenAnime = viewModel.topTenAnime.collectAsState()
    val topTenAiringAnime = viewModel.topTenAiringAnime.collectAsState()
    val topTenUpcomingAnime = viewModel.topTenUpcomingAnime.collectAsState()
    val topTenMovies = viewModel.topTenMovies.collectAsState()
    val tenSuggestedAnime = viewModel.tenSuggestedAnime.collectAsState()

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        SearchBar(
            query = "",
            onQueryChange = {},
            onSearch = {},
            active = false,
            onActiveChange = {},
            placeholder = { Text("Search") },
        ) {

        }

        HorizontalDivider(modifier = Modifier.padding(4.dp))

        Text("Top 10 Anime")

        CenterHeroCarousel(topTenAnime.value) { animeId ->
            navController.navigate("anime_details/$animeId")
        }

        HorizontalDivider(modifier = Modifier.padding(4.dp))

        Text("Top 10 Airing")

        CenterHeroCarousel(topTenAiringAnime.value) { animeId ->
            navController.navigate("anime_details/$animeId")
        }

        HorizontalDivider(modifier = Modifier.padding(4.dp))

        Text("Top 10 Upcoming")

        CenterHeroCarousel(topTenUpcomingAnime.value) { animeId ->
            navController.navigate("anime_details/$animeId")
        }

        HorizontalDivider(modifier = Modifier.padding(4.dp))

        Text("Top 10 Movies")

        CenterHeroCarousel(topTenMovies.value) { animeId ->
            navController.navigate("anime_details/$animeId")
        }

        HorizontalDivider(modifier = Modifier.padding(4.dp))

        Text("Top 10 Suggested For You")

        CenterHeroCarousel(tenSuggestedAnime.value) { animeId ->
            navController.navigate("anime_details/$animeId")
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CenterHeroCarousel(
    tenSuggestedAnime: List<AnimeListItem>,
    onItemClicked: (Int) -> Unit = {}
) {
    val listState = rememberLazyListState()
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    LazyRow(
        state = listState,
        flingBehavior = flingBehavior,
        contentPadding = PaddingValues(horizontal = 48.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 16.dp)
    ) {
        itemsIndexed(tenSuggestedAnime) { index, item ->
            val layoutInfo = listState.layoutInfo
            val itemInfo = layoutInfo.visibleItemsInfo.find { it.index == index }

            // Calculate smooth scale based on pixel distance from center
            val scale = if (itemInfo != null) {
                val viewportCenter = layoutInfo.viewportStartOffset + layoutInfo.viewportEndOffset / 2
                val itemCenter = itemInfo.offset + itemInfo.size / 2
                val distance = (itemCenter - viewportCenter).toFloat()
                val maxDistance = layoutInfo.viewportEndOffset / 2f

                // Normalize and invert to get scale from 0.85 to 1.0
                val normalized = (1f - (distance / maxDistance).absoluteValue).coerceIn(0f, 1f)
                0.85f + (0.15f * normalized)
            } else 0.85f // Default for offscreen

            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .width(160.dp)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    }
                    .clickable { onItemClicked(item.node.id) }
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .height(216.dp)
                            .fillMaxWidth()
                    ) {
                        AsyncImage(
                            model = item.node.mainPicture.large,
                            contentDescription = "Anime poster",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.matchParentSize(),
                            placeholder = painterResource(R.drawable.ic_launcher_foreground)
                        )

                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f)),
                                        startY = 100f
                                    )
                                )
                        )

                        Text(
                            text = item.node.title,
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Light
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .padding(8.dp)
                                .align(Alignment.BottomCenter)
                        )
                    }
                }
            }
        }
    }
}