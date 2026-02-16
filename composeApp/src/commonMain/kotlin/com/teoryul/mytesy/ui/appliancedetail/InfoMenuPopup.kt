package com.teoryul.mytesy.ui.appliancedetail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InfoMenuPopup(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onDismiss: () -> Unit,
    onItemClick: (InfoMenuItem) -> Unit,
    items: List<InfoMenuItem> = defaultInfoItems,
    bottomBarHeight: Dp = 64.dp,
    horizontalPadding: Dp = 4.dp,
    popupVerticalGap: Dp = 4.dp
) {
    if (!expanded) return

    Box(
        modifier = modifier
            .fillMaxSize()
            // tap outside dismissal
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onDismiss
            )
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding)
                .padding(bottom = bottomBarHeight + popupVerticalGap),
            verticalAlignment = Alignment.Bottom
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier.weight(3f),
                contentAlignment = Alignment.BottomEnd
            ) {
                this@Row.AnimatedVisibility(
                    visible = expanded,
                    enter = fadeIn(tween(140)) + scaleIn(
                        initialScale = 0.92f,
                        animationSpec = tween(160)
                    ),
                    exit = fadeOut(tween(120)) + scaleOut(
                        targetScale = 0.92f,
                        animationSpec = tween(140)
                    )
                ) {
                    // "Wrap content based on longest item" behavior:
                    // - use wrapContentWidth() so the card can grow
                    // - but cap it so it doesn't get too wide on tablets
                    InfoMenuCard(
                        modifier = Modifier
                            .wrapContentWidth()
                            .widthIn(min = 200.dp, max = 280.dp)
                            // consume inside taps so they don't dismiss
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = { /* consume */ }
                            )
                            .shadow(
                                elevation = 12.dp,
                                shape = RoundedCornerShape(14.dp),
                                clip = false
                            )
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFFF6F6F6))
                            .padding(vertical = 6.dp),
                        items = items,
                        onItemClick = onItemClick
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoMenuCard(
    modifier: Modifier = Modifier,
    items: List<InfoMenuItem>,
    onItemClick: (InfoMenuItem) -> Unit
) {
    LazyColumn(
        modifier = modifier.heightIn(max = 340.dp), // scroll after this
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        itemsIndexed(items, key = { _, item -> item.id }) { index, item ->
            InfoMenuRow(
                item = item,
                onClick = { onItemClick(item) }
            )

            if (index != items.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    thickness = 1.dp,
                    color = Color.Black.copy(alpha = 0.08f)
                )
            }
        }
    }
}

@Composable
private fun InfoMenuRow(
    item: InfoMenuItem,
    onClick: () -> Unit
) {
    val tint = if (item.isDestructive) Color(0xFFeb445a) else Color.Gray
    val textColor = if (item.isDestructive) Color(0xFFeb445a) else Color.Gray

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            imageVector = item.icon,
            contentDescription = item.label,
            tint = tint
        )
        Text(
            text = item.label,
            fontSize = 12.sp,
            color = textColor,
            maxLines = 1
        )
    }
}