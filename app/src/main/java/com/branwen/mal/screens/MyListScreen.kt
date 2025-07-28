package com.branwen.mal.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.branwen.mal.components.ListItem
import com.branwen.mal.components.StatusFilterChips
import com.branwen.mal.viewmodels.MyListViewModel

@Composable
fun MyListScreen(
    viewModel: MyListViewModel = hiltViewModel(),
    navigate: (Int) -> Unit
) {
    val selectedStatus by viewModel.selectedStatus.collectAsState()
    val filteredAnimeList by viewModel.filteredAnimeList.collectAsState()

    val listState = rememberLazyListState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        StatusFilterChips(
            selectedStatus = selectedStatus,
            onStatusSelected = { status ->
                viewModel.onStatusSelected(status)
            }
        )

        LazyColumn(
            state = listState,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .padding(12.dp, 0.dp, 12.dp, 12.dp)
                .fillMaxSize()
        ) {
            items(filteredAnimeList) { item ->
                ListItem(animeItem = item, onItemClicked = { navigate(item.node.id) })
            }
        }
    }
}