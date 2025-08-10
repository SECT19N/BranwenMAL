package com.branwen.mal.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * A composable function that displays a row of filter chips for different anime or manga statuses.
 * The available statuses depend on the `checkedState` parameter.
 *
 * @param selectedStatus The currently selected status. This will determine which chip is highlighted.
 * @param checkedState A boolean indicating the type of media. `true` for anime statuses, `false` for manga statuses.
 * @param onStatusSelected A callback function that is invoked when a status chip is clicked.
 * It receives the string representation of the selected status.
 */
@Composable
fun StatusFilterChips(
    selectedStatus: String,
    checkedState: Boolean,
    onStatusSelected: (String) -> Unit
) {
    val statuses = when (checkedState) {
        true -> listOf("all", "watching", "completed", "on_hold", "dropped", "plan_to_watch")
        false -> listOf("all", "reading", "completed", "on_hold", "dropped", "plan_to_read")
    }

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp, 4.dp)
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
                            "reading" -> "Reading"
                            "completed" -> "Completed"
                            "on_hold" -> "On Hold"
                            "dropped" -> "Dropped"
                            "plan_to_watch" -> "Plan to Watch"
                            "plan_to_read" -> "Plan to Read"
                            else -> "All"
                        }
                    )
                },
                leadingIcon = {
                    when (status) {
                        selectedStatus -> {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Selected",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    }
                }
            )
        }
    }
}