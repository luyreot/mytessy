package com.teoryul.mytesy.ui.appliancedetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teoryul.mytesy.ui.common.LoadImageBitmap
import com.teoryul.mytesy.ui.common.SafeImage

@Composable
fun RowScope.EcoModesQuickAction(
    selectedEcoMode: EcoMode,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .clickable(enabled = !isLoading, onClick = onClick)
            .padding(vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(22.dp),
                strokeWidth = 2.dp,
                color = Color(0xFF8DC63F)
            )
            Spacer(Modifier.height(4.dp))
            Text(text = "Updating", fontSize = 12.sp, color = Color.Gray, maxLines = 1)
            return@Column
        }

        SafeImage(
            imageBitmap = LoadImageBitmap(selectedEcoMode.icon),
            imageModifier = Modifier.size(24.dp),
            contentScale = ContentScale.Fit,
            contentDescription = selectedEcoMode.icon.contentDescription,
            colorFilter = ColorFilter.tint(
                if (selectedEcoMode == EcoMode.Off)
                    Color.Gray
                else
                    Color(0xFF8DC63F)
            ),
            fallbackContent = {}
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = selectedEcoMode.label,
            fontSize = 12.sp,
            color = Color.Gray,
            maxLines = 1
        )
    }
}