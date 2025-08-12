package com.branwen.mal.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableChipColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
@OptIn(ExperimentalMaterial3Api::class)
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

    val animationSpec = remember { tween<Color>(durationMillis = 133) }

    @Composable
    fun animatedFilterChipColors(selected: Boolean): SelectableChipColors {
        // Define target colors based on MaterialTheme and typical FilterChip defaults
        val targetContainerColor = if (selected) {
            MaterialTheme.colorScheme.secondaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceContainerLow
        }

        val targetLabelColor = if (selected) {
            MaterialTheme.colorScheme.onSecondaryContainer
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        }

        // Icon color often matches label color for FilterChips
        val targetIconColor = targetLabelColor

        val animatedContainerColor by animateColorAsState(
            targetValue = targetContainerColor,
            animationSpec = animationSpec,
            label = "ChipContainerColor"
        )
        val animatedLabelColor by animateColorAsState(
            targetValue = targetLabelColor,
            animationSpec = animationSpec,
            label = "ChipLabelColor"
        )
        val animatedIconColor by animateColorAsState(
            targetValue = targetIconColor,
            animationSpec = animationSpec,
            label = "ChipIconColor"
        )

        return FilterChipDefaults.filterChipColors(
            containerColor = animatedContainerColor,
            labelColor = animatedLabelColor,
            iconColor = animatedIconColor,
            selectedContainerColor = animatedContainerColor,
            selectedLabelColor = animatedLabelColor,
            selectedLeadingIconColor = animatedIconColor
            // Disabled colors are derived by default based on the enabled ones.
        )
    }

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp, 4.dp)
    ) {
        items(statuses) { status ->
            val selected = status == selectedStatus
            FilterChip(
                selected = selected,
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
                colors = animatedFilterChipColors(selected = selected),
                leadingIcon = {
                    when (selected) {
                        true -> Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Selected",
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )

                        false -> Unit
                    }
                }
            )
        }
    }
}