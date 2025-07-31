package com.branwen.mal.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.branwen.mal.components.ListItem
import com.branwen.mal.components.StatusFilterChips
import com.branwen.mal.viewmodels.MyListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyListScreen(
    viewModel: MyListViewModel = hiltViewModel(),
    navigate: (Int) -> Unit
) {
    val selectedStatus by viewModel.selectedStatus.collectAsState()
    val filteredAnimeList by viewModel.filteredAnimeList.collectAsState()

    val loading by viewModel.loading.collectAsState()

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
                        Text(
                            text = if (checkedState) "A" else "M"
                        )
                    },
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 0.dp)
                )
            },
            modifier = Modifier
        )

        StatusFilterChips(
            selectedStatus = selectedStatus,
            onStatusSelected = { status ->
                viewModel.onStatusSelected(status)
            }
        )

        Box(modifier = Modifier.fillMaxSize()) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    state = listState,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .padding(12.dp, 0.dp, 12.dp, 12.dp)
                        .fillMaxSize()
                ) {
                    items(filteredAnimeList) { item ->
                        ListItem(animeItem = item, onItemClicked = { navigate(item.id) })
                    }
                }
            }
        }
    }
}