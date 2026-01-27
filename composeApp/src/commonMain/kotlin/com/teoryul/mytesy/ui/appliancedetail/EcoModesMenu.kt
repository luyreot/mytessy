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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PowerSettingsNew
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teoryul.mytesy.ui.common.LoadImageBitmap
import com.teoryul.mytesy.ui.common.SafeImage

@Composable
fun EcoModesPopup(
    expanded: Boolean,
    isSelectedEcoMode: Boolean,
    onSelect: (EcoMode) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
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
        // Anchor row with the same padding as your bottom bar,
        // and use 4 equal weights so the first cell is exactly the first bottom item.
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding)
                .padding(bottom = bottomBarHeight + popupVerticalGap),
            verticalAlignment = Alignment.Bottom
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.BottomCenter
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
                    EcoModesMenu(
                        modifier = Modifier
                            .fillMaxWidth() // 1/4 of screen width
                            // consume taps inside popup so they don't dismiss
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = { /* consume */ }
                            )
                            .shadow(
                                elevation = 10.dp,
                                shape = RoundedCornerShape(14.dp),
                                clip = false
                            )
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFFF6F6F6))
                            .padding(vertical = 8.dp),
                        isSelectedEcoMode = isSelectedEcoMode,
                        onSelect = onSelect
                    )
                }
            }

            // Remaining three quarters are empty, just to keep the anchor alignment correct
            Spacer(modifier = Modifier.weight(3f))
        }
    }
}

@Composable
fun EcoModesMenu(
    modifier: Modifier = Modifier,
    isSelectedEcoMode: Boolean,
    onSelect: (EcoMode) -> Unit
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EcoModeMenuItem(mode = EcoMode.EcoSmart, onClick = { onSelect(EcoMode.EcoSmart) })
        EcoModeMenuItem(mode = EcoMode.EcoComfort, onClick = { onSelect(EcoMode.EcoComfort) })
        EcoModeMenuItem(mode = EcoMode.EcoNight, onClick = { onSelect(EcoMode.EcoNight) })
        EcoModeMenuItem(mode = EcoMode.Vacation, onClick = { onSelect(EcoMode.Vacation) })

        if (isSelectedEcoMode) {
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                thickness = 1.dp,
                color = Color.Black.copy(alpha = 0.08f)
            )

            DeactivateMenuItem { onSelect(EcoMode.Off) }
        }
    }
}

@Composable
private fun EcoModeMenuItem(
    mode: EcoMode,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        SafeImage(
            imageBitmap = LoadImageBitmap(mode.icon),
            imageModifier = Modifier.size(24.dp),
            contentScale = ContentScale.Fit,
            contentDescription = mode.icon.contentDescription,
            colorFilter = ColorFilter.tint(Color(0xFF8DC63F)),
            fallbackContent = {}
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = mode.label,
            fontSize = 12.sp,
            color = Color.Gray,
            maxLines = 1
        )
    }
}

@Composable
private fun DeactivateMenuItem(
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            imageVector = Icons.Default.PowerSettingsNew,
            contentDescription = "Deactivate",
            tint = Color(0xFFeb445a)
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Deactivate",
            fontSize = 12.sp,
            color = Color.Gray,
            maxLines = 1
        )
    }
}