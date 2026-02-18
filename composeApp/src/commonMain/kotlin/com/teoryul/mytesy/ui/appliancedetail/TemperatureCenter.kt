package com.teoryul.mytesy.ui.appliancedetail

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@Composable
fun TemperatureCenter(
    modifier: Modifier = Modifier,
    currentTemp: String?,
    targetTemp: String?,
    isPowerOn: Boolean,
    statusText: String,
    isHeating: Boolean,
    isManualProgramEnabled: Boolean,
    isViewingActiveProgram: Boolean,
    onTargetTempChange: (Float) -> Unit
) {
    val minTemp = 9f
    val maxTemp = 75f

    val parsedTarget = parseTempC(targetTemp)
        ?.coerceIn(minTemp, maxTemp)
        ?: minTemp

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        TemperatureCenterContent(
            currentTemp = currentTemp,
            targetTemp = parsedTarget,
            isPowerOn = isPowerOn,
            statusText = statusText,
            isHeating = isHeating,
            isManualProgramEnabled = isManualProgramEnabled,
            isViewingActiveProgram = isViewingActiveProgram,
            onTargetTempChange = onTargetTempChange
        )
    }
}

@Composable
private fun TemperatureCenterContent(
    modifier: Modifier = Modifier,
    currentTemp: String?,
    targetTemp: Float,
    isPowerOn: Boolean,
    statusText: String,
    isHeating: Boolean,
    isManualProgramEnabled: Boolean,
    isViewingActiveProgram: Boolean,
    onTargetTempChange: (Float) -> Unit
) {
    val minTemp = 9f
    val maxTemp = 75f

    var localTargetTemp by remember(targetTemp) { mutableStateOf(targetTemp) }

    val (main, frac) = splitTemp(currentTemp)
    val targetDisplay = localTargetTemp.roundToInt().toString()

    val tempColor = Color(0xFF5E006C)
    val tempLabelColor = Color.Gray

    val heatingPulseInfinite = rememberInfiniteTransition(label = "heatingPulse")
    val heatingAlpha by heatingPulseInfinite.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "heatingAlpha"
    )

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically)
    ) {
        // Temperature information
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                fontSize = 24.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = tempColor
                            )
                        ) {
                            append(main)
                        }
                        if (frac != null) {
                            withStyle(
                                SpanStyle(fontSize = 16.sp, color = tempColor)
                            ) {
                                append(".$frac")
                            }
                        }
                        withStyle(
                            SpanStyle(
                                fontSize = 24.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = tempColor
                            )
                        ) {
                            append("°C")
                        }
                    },
                    maxLines = 1
                )
                Text(
                    text = "Current",
                    fontSize = 16.sp,
                    color = tempLabelColor,
                    fontWeight = FontWeight.Normal
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                fontSize = 24.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = tempColor
                            )
                        ) {
                            append(targetDisplay)
                        }
                        withStyle(
                            SpanStyle(
                                fontSize = 24.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = tempColor
                            )
                        ) {
                            append("°C")
                        }
                    },
                    maxLines = 1
                )
                Text(
                    text = "Target",
                    fontSize = 16.sp,
                    color = tempLabelColor,
                    fontWeight = FontWeight.Normal
                )
            }
        }

        // Temperature status
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (isHeating) {
                Icon(
                    modifier = Modifier.size(36.dp),
                    imageVector = Icons.Rounded.LocalFireDepartment,
                    contentDescription = "Heating",
                    tint = Color(0xFFeb445a).copy(alpha = heatingAlpha)
                )
            } else {
                Icon(
                    modifier = Modifier.size(36.dp),
                    imageVector = Icons.Rounded.CheckCircle,
                    contentDescription = "Ready",
                    tint = Color(0xFF2E7D32)
                )
            }

            val labelColor = if (isHeating) Color(0xFFeb445a) else Color(0xFF2E7D32)
            Text(
                text = statusText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = labelColor.copy(alpha = if (isHeating) heatingAlpha else 1f)
            )
        }

        // Manual target temperature controls
        if (isManualProgramEnabled && isViewingActiveProgram) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    modifier = Modifier.size(48.dp),
                    onClick = {
                        val newTemp = (localTargetTemp - 1f).coerceIn(minTemp, maxTemp)
                        localTargetTemp = newTemp
                        onTargetTempChange(newTemp)
                    }
                ) {
                    Icon(
                        modifier = Modifier.fillMaxSize(),
                        imageVector = Icons.Rounded.Remove,
                        contentDescription = "Decrease target temperature",
                        tint = Color(0xFF1565C0)
                    )
                }

                IconButton(
                    modifier = Modifier.size(48.dp),
                    onClick = {
                        val newTemp = (localTargetTemp + 1f).coerceIn(minTemp, maxTemp)
                        localTargetTemp = newTemp
                        onTargetTempChange(newTemp)
                    }
                ) {
                    Icon(
                        modifier = Modifier.fillMaxSize(),
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "Increase target temperature",
                        tint = Color(0xFFeb445a)
                    )
                }
            }
        }
    }
}

private fun parseTempC(raw: String?): Float? =
    raw?.trim()
        ?.replace(",", ".")
        ?.toFloatOrNull()

private fun splitTemp(raw: String?): Pair<String, String?> {
    val cleaned = raw?.trim()?.replace(",", ".").orEmpty()
    if (cleaned.isEmpty()) return "--" to null
    val parts = cleaned.split(".", limit = 2)
    val main = parts.getOrNull(0)?.takeIf { it.isNotBlank() } ?: "--"
    val frac = parts.getOrNull(1)
        ?.filter { it.isDigit() }
        ?.take(1)
        ?.takeIf { it.isNotEmpty() }
    return main to frac
}