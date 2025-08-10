package com.branwen.mal.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.branwen.mal.components.MyListScreenContent
import com.branwen.mal.components.StatusFilterChips
import com.branwen.mal.viewmodels.MyListViewModel

/**
 * Composable function that displays the user's anime list.
 *
 * This screen allows the user to view their anime list, filter it by status,
 * and navigate to the details of a specific anime. It also supports pull-to-refresh
 * functionality.
 *
 * @param viewModel The [MyListViewModel] that provides the data and logic for this screen.
 *                  It is injected using Hilt.
 * @param navigate A lambda function that takes an anime ID (Int) and navigates to the
 *                 corresponding anime details screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyListScreen(
    viewModel: MyListViewModel = hiltViewModel(),
    navigate: (Int) -> Unit
) {
    val selectedStatus by viewModel.selectedStatus.collectAsState()
    val filteredAnimeList by viewModel.filteredAnimeList.collectAsState()
    val filteredMangaList by viewModel.filteredMangaList.collectAsState()

    val loading by viewModel.loading.collectAsState()

    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val pullState = rememberPullToRefreshState()

    val listState = rememberLazyListState()

    var checkedState by remember { viewModel.listSwitchChecked }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                titleContentColor = MaterialTheme.colorScheme.onSurface,
                navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                actionIconContentColor = MaterialTheme.colorScheme.onSurface
            ),
            title = {
                Text("MAL")
            },
            navigationIcon = {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 0.dp)
                )
            },
            actions = {
                Switch(
                    checked = checkedState,
                    onCheckedChange = {
                        checkedState = it
                    },
                    thumbContent = {
                        TooltipBox(
                            modifier = Modifier,
                            positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                            tooltip = {
                                PlainTooltip { Text("List Type") }
                            },
                            state = rememberTooltipState()
                        ) {
                            Text(
                                text = if (checkedState) "A" else "M"
                            )
                        }
                    },
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 0.dp)
                )
            },
            modifier = Modifier
        )

        StatusFilterChips(
            selectedStatus = selectedStatus,
            checkedState = checkedState,
            onStatusSelected = { status ->
                viewModel.onStatusSelected(status)
            }
        )

        MyListScreenContent(
            isRefreshing = isRefreshing,
            loading = loading,
            filteredAnimeList = filteredAnimeList,
            filteredMangaList = filteredMangaList,
            pullState = pullState,
            listState = listState,
            checkedState = checkedState,
            onRefresh = { viewModel.onPullToRefresh() },
            onItemClicked = { id -> navigate(id) },
            onProgressIncremented = { anime ->
                viewModel.onProgressIncrement(anime)
            }
        )
    }
}