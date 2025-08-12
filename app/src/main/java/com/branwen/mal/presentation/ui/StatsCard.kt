package com.branwen.mal.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
private fun StatsCard(

) {
    Card(
        modifier = Modifier
            .width(360.dp)
            .height(240.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            Text(
                text = "Anime Stats", fontWeight = FontWeight.Bold, fontSize = 20.sp,
            )

            StackedProgressBar()

            Row {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    StatItem(Color(0xFF4CD137), "Watching", "5")
                    StatItem(Color(0xFF00A8FF), "Completed", "5")
                    StatItem(Color(0xFFFBC531), "On-Hold", "5")
                }

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    StatItem(Color(0xFFE84118), "Dropped", "5")
                    StatItem(Color(0xFF707070), "Planned", "5")
                    StatItem(Color(0x00FFFFFF), "Episodes Watched", "5")
                }
            }
        }
    }
}

@Composable
fun StackedProgressBar() {
    val data = mapOf(
        "Green" to 5,
        "Blue" to 76,
        "Yellow" to 6,
        "Red" to 3,
        "Gray" to 34
    )

    val total = data.values.sum().toFloat()

    val colorMap = mapOf(
        "Green" to Color(0xFF4CD137),
        "Blue" to Color(0xFF00A8FF),
        "Yellow" to Color(0xFFFBC531),
        "Red" to Color(0xFFE84118),
        "Gray" to Color(0xFF707070),
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .clip(RoundedCornerShape(12.dp)),
    ) {
        data.forEach { (key, value) ->
            val percentage = value / total

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(percentage)
                    .fillMaxHeight()
                    .background(colorMap[key] ?: Color.Gray)
            )
        }
    }
}

@Composable
fun StatItem(
    statColor: Color,
    statName: String,
    statNumber: String,
) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .drawWithCache {
                    onDrawBehind {
                        drawCircle(
                            color = statColor,
                            radius = size.width / 2,
                        )
                    }
                }
                .padding(8.dp, 0.dp, 0.dp, 0.dp)
        )

        Text(text = statName)

        Text(text = statNumber)
    }
}