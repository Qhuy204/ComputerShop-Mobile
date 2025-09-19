package com.example.computerstore.screens.tabs

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@Composable
fun LoginSignupTabs(
    selectedTab: String,
    onTabSelected: (String) -> Unit
) {
    val tabs = listOf("login", "signup")
    val selectedIndex = tabs.indexOf(selectedTab)

    val tabWidth = remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    // Animation trượt ngang
    val offsetX by animateDpAsState(
        targetValue = tabWidth.value * selectedIndex,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy, // không nảy
            stiffness = Spring.StiffnessLow
        ),
        label = "offsetX"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFE9EEF5), RoundedCornerShape(9999.dp))
            .padding(4.dp)
            .onGloballyPositioned { layoutCoordinates ->
                tabWidth.value = with(density) { layoutCoordinates.size.width.toDp() / 2 }
            }
    ) {
        // Indicator trắng
        if (tabWidth.value > 0.dp) {
            Box(
                modifier = Modifier
                    .offset(x = offsetX)
                    .width(tabWidth.value)
                    .clip(RoundedCornerShape(9999.dp))
                    .background(Color.White)
                    .padding(vertical = 10.dp)
                    .height(24.dp)

            )
        }

        // Text layer trên cùng
        Row(modifier = Modifier.fillMaxWidth()) {
            TabText(
                text = "Đăng nhập",
                isSelected = selectedTab == "login",
                onClick = { onTabSelected("login") },
                modifier = Modifier.weight(1f)
            )
            TabText(
                text = "Đăng ký",
                isSelected = selectedTab == "signup",
                onClick = { onTabSelected("signup") },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun TabText(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(9999.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onClick() }
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) Color(0xFF1A73E8) else Color(0xFF5F6368),
            fontWeight = FontWeight.SemiBold
        )
    }
}
