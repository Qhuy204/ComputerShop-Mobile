package com.example.computerstore.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.overscroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlin.math.ceil

data class HorizontalItem(
    val name: String,
    val iconUrl: String
)

@Composable
fun ItemList(
    title: String,
    items: List<HorizontalItem>,
    onItemClick: (HorizontalItem) -> Unit // callback click
) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )

        // Chia 2 hàng
        val half = ceil(items.size / 2f).toInt()
        val firstRow = items.take(half)
        val secondRow = items.drop(half)

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .overscroll(overscrollEffect  = null)
            ,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Hàng trên
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        firstRow.forEach { item ->
                            ItemBox(item, onClick = { onItemClick(item) })
                        }
                    }

                    // Hàng dưới
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        secondRow.forEach { item ->
                            ItemBox(item, onClick = { onItemClick(item) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ItemBox(item: HorizontalItem, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(80.dp)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(10.dp))
                .border(0.5.dp, Color.LightGray, RoundedCornerShape(10.dp))
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = item.iconUrl,
                contentDescription = item.name,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = item.name,
            fontSize = 12.sp,
            maxLines = 1
        )
    }
}
