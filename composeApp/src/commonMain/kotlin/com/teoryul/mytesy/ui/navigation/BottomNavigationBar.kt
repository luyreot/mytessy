package com.teoryul.mytesy.ui.navigation

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun BottomNavigationBar(
    currentScreen: Screen,
    onTabSelected: (BottomNavItem) -> Unit
) {
    NavigationBar {
        BottomNavItem.items.forEach { item ->
            val isSelected = currentScreen::class == item.screen::class
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(item) },
                icon = {
                    AnimatedBottomBarIcon(
                        icon = item.icon,
                        contentDescription = item.label,
                        selected = isSelected
                    )
                },
                label = {
                    Text(text = item.label)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF8DC63F),
                    selectedTextColor = Color(0xFF8DC63F),
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray
                )
            )
        }
    }
}

@Composable
private fun AnimatedBottomBarIcon(
    icon: ImageVector,
    contentDescription: String,
    selected: Boolean
) {
    val scale by animateFloatAsState(
        targetValue = if (selected) 1.12f else 1.0f,
        animationSpec = spring(
            dampingRatio = 0.75f, // subtle bounce
            stiffness = 500f
        ),
        label = "bottomBarIconScale"
    )

    val offsetY by animateDpAsState(
        targetValue = if (selected) (-1).dp else 0.dp,
        animationSpec = tween(durationMillis = 180),
        label = "bottomBarIconOffsetY"
    )

    Icon(
        modifier = Modifier
            .offset(y = offsetY)
            .scale(scale),
        imageVector = icon,
        contentDescription = contentDescription
    )
}