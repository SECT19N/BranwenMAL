package com.branwen.mal.screens

import android.content.Context
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.branwen.mal.components.ListItem
import com.branwen.mal.components.StatusFilterChips
import com.branwen.mal.data.repo.AnimeListRepository
import com.branwen.mal.viewmodels.MyListViewModel
import com.branwen.mal.viewmodels.factory.MyListViewModelFactory

@Composable
fun MyListScreen() {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("bran_mal_prefs", Context.MODE_PRIVATE)

    val repo = remember { AnimeListRepository(sharedPreferences) }

    val viewModel: MyListViewModel = viewModel(
        factory = MyListViewModelFactory(repo)
    )

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
                ListItem(animeItem = item)
            }
        }
    }
}