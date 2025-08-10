package com.branwen.mal.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.branwen.mal.models.domain.MyAnimeListItem
import com.branwen.mal.models.domain.MyMangaListItem

/**
 * Composable function that displays the content of the anime/manga list screen.
 * It handles the display of a list of items, loading indicators, and pull-to-refresh functionality.
 *
 * @param isRefreshing A boolean indicating whether the list is currently being refreshed.
 * @param loading A boolean indicating whether the initial data is being loaded.
 * @param filteredAnimeList A list of [MyAnimeListItem] to be displayed when `checkedState` is true.
 * @param filteredMangaList A list of [MyMangaListItem] to be displayed when `checkedState` is false.
 * @param pullState The state for the pull-to-refresh mechanism.
 * @param listState The state for the lazy list, used for scroll position and other list-related operations.
 * @param checkedState A boolean indicating whether to display the anime list (true) or manga list (false).
 * @param onRefresh A lambda function to be invoked when a refresh is triggered (e.g., by pulling down).
 * @param onItemClicked A lambda function to be invoked when an item in the list is clicked. It takes the ID of the clicked item as a parameter.
 * @param onProgressIncremented A lambda function to be invoked when the progress of an anime item is incremented.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyListScreenContent(
    isRefreshing: Boolean,
    loading: Boolean,
    filteredAnimeList: List<MyAnimeListItem>,
    filteredMangaList: List<MyMangaListItem>,
    pullState: PullToRefreshState,
    listState: LazyListState,
    checkedState: Boolean,
    onRefresh: () -> Unit,
    onItemClicked: (Int) -> Unit,
    onProgressIncremented: (MyAnimeListItem) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        PullToRefreshBox(
            state = pullState,
            isRefreshing = isRefreshing,
            onRefresh = { onRefresh() },
            modifier = Modifier.fillMaxSize()
        ) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    state = listState,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .padding(12.dp, 0.dp, 12.dp, 12.dp)
                        .fillMaxSize()
                ) {
                    when (checkedState) {
                        true -> items(filteredAnimeList) { item ->
                            AnimeListItem(
                                animeItem = item,
                                onItemClicked = { onItemClicked(item.id) },
                                onProgressIncremented = { onProgressIncremented(item) }
                            )
                        }

                        false -> items(filteredMangaList) { item ->
                            MangaListItem(
                                mangaItem = item,
                                onItemClicked = { /* TODO */ },
                                onProgressIncremented = { /* TODO */ }
                            )
                        }
                    }
                }
            }
        }
    }
}