package com.teoryul.mytesy.ui.appliancedetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teoryul.mytesy.ui.common.AppImage
import com.teoryul.mytesy.ui.common.LoadImageBitmap
import com.teoryul.mytesy.ui.common.SafeImage

@Composable
fun TemperatureCenter(
    modifier: Modifier = Modifier,
    currentTemp: String?,
    targetTemp: String?,
    isPowerOn: Boolean,
    statusText: String,
    isHeating: Boolean
) {
    val (main, frac) = splitTemp(currentTemp) // e.g. "52.6" -> ("52", "6")

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SafeImage(
            imageBitmap = LoadImageBitmap(AppImage.HeatIcon),
            imageModifier = Modifier.size(48.dp),
            contentScale = ContentScale.Fit,
            colorFilter = when {
                isHeating -> ColorFilter.tint(Color(0xFFeb445a))
                isPowerOn -> ColorFilter.tint(Color(0xFF3CAF3D))
                else -> ColorFilter.tint(Color.Gray)
            },
            contentDescription = AppImage.HeatIcon.contentDescription,
            fallbackContent = {}
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = buildAnnotatedString {
                // Main integer part (big)
                append(main)

                // Decimal dot + fraction (smaller)
                if (frac != null) {
                    pushStyle(SpanStyle(fontSize = 24.sp))
                    append(".")
                    append(frac)
                    pop()
                }

                // Celsius (smaller)
                pushStyle(SpanStyle(fontWeight = FontWeight.W300))
                append("°C")
                pop()
            },
            fontSize = 48.sp,
            lineHeight = 50.sp,
            maxLines = 1,
            color = when {
                isHeating -> Color(0xFFeb445a)
                isPowerOn -> Color(0xFF3CAF3D)
                else -> Color.Gray
            }
        )

        Spacer(Modifier.height(8.dp))

        val targetDisplay = targetTemp?.trim()?.takeIf { it.isNotEmpty() } ?: "--"
        Text(
            text = "Target: $targetDisplay°C",
            fontSize = 18.sp,
            color = Color.Gray,
            maxLines = 1
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = statusText,
            fontSize = 16.sp,
            maxLines = 1,
            color = when {
                isHeating -> Color(0xFFeb445a)
                isPowerOn -> Color(0xFF3CAF3D)
                else -> Color.Gray
            }
        )
    }
}

private fun splitTemp(raw: String?): Pair<String, String?> {
    val cleaned = raw?.trim()?.replace(",", ".").orEmpty()
    if (cleaned.isEmpty()) return "--" to null

    val parts = cleaned.split(".", limit = 2)
    val main = parts.getOrNull(0)?.takeIf { it.isNotBlank() } ?: "--"
    val frac = parts.getOrNull(1)?.filter { it.isDigit() }?.take(1)?.takeIf { it.isNotEmpty() }
    return main to frac
}