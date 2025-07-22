package com.branwen.mal.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StatusFilterChips(
    selectedStatus: String,
    onStatusSelected: (String) -> Unit
) {
    val statuses = listOf("all", "watching", "completed", "on_hold", "dropped", "plan_to_watch")

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp, 8.dp)
    ) {
        items(statuses) { status ->
            FilterChip(
                selected = status == selectedStatus,
                onClick = { onStatusSelected(status) },
                label = {
                    Text(
                        text = when (status) {
                            "all" -> "All"
                            "watching" -> "Watching"
                            "completed" -> "Completed"
                            "on_hold" -> "On Hold"
                            "dropped" -> "Dropped"
                            "plan_to_watch" -> "Plan to Watch"
                            else -> "All"
                        }
                    )
                }
            )
        }
    }
}