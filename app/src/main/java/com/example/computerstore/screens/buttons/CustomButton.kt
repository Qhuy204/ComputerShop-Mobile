package com.example.computerstore.screens.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    iconRes: Int? = null,
    imageVector: ImageVector? = null,
    contentDescription: String? = null,
    backgroundColor: Color = Color(0xFFE8EAE9),
    iconTint: Color = Color.Black,
    size: Dp = 48.dp,
    shape: androidx.compose.ui.graphics.Shape = CircleShape
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(shape)
            .background(backgroundColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        when {
            iconRes != null -> Icon(
                painter = painterResource(id = iconRes),
                contentDescription = contentDescription,
                tint = iconTint
            )
            imageVector != null -> Icon(
                imageVector = imageVector,
                contentDescription = contentDescription,
                tint = iconTint
            )
        }
    }
}
