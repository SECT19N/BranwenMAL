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

/**
 * Composable function that displays the content of the anime list screen.
 * It handles the display of a list of anime items, loading indicators, and pull-to-refresh functionality.
 *
 * @param isRefreshing A boolean indicating whether the list is currently being refreshed.
 * @param loading A boolean indicating whether the initial data is being loaded.
 * @param filteredAnimeList A list of [MyAnimeListItem] to be displayed.
 * @param pullState The state for the pull-to-refresh mechanism.
 * @param listState The state for the lazy list, used for scroll position and other list-related operations.
 * @param onRefresh A lambda function to be invoked when a refresh is triggered (e.g., by pulling down).
 * @param onItemClicked A lambda function to be invoked when an anime item in the list is clicked. It takes the ID of the clicked item as a parameter.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeListScreenContent(
    isRefreshing: Boolean,
    loading: Boolean,
    filteredAnimeList: List<MyAnimeListItem>,
    pullState: PullToRefreshState,
    listState: LazyListState,
    onRefresh: () -> Unit,
    onItemClicked: (Int) -> Unit,
    onProgressIncremented: (MyAnimeListItem) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        PullToRefreshBox(
            state = pullState,
            isRefreshing = isRefreshing,
            onRefresh = { onRefresh },
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
                    items(filteredAnimeList) { item ->
                        ListItem(
                            animeItem = item,
                            onItemClicked = { onItemClicked(item.id) },
                            onProgressIncremented = { onProgressIncremented(item) }
                        )
                    }
                }
            }
        }
    }
}