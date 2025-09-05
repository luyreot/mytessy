package com.teoryul.mytesy.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.teoryul.mytesy.domain.appliance.ApplianceEntity
import com.teoryul.mytesy.ui.common.AppImage
import com.teoryul.mytesy.ui.common.LoadImageBitmap
import com.teoryul.mytesy.ui.common.SafeImage
import kotlin.math.roundToInt

@Composable
fun ApplianceList(
    items: List<ApplianceEntity>,
    applianceUiFlags: Map<String, ApplianceUiFlags>,
    onApplianceClick: (ApplianceEntity) -> Unit,
    onPowerToggle: (ApplianceEntity, enabled: Boolean) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = items,
            key = { it.deviceSerial }
        ) { appliance ->
            val flags = applianceUiFlags[appliance.id] ?: ApplianceUiFlags()

            ApplianceCard(
                appliance = appliance,
                uiFlags = flags,
                onRowClick = { onApplianceClick(appliance) },
                onPowerToggle = { enabled -> onPowerToggle(appliance, enabled) }
            )
        }
    }
}

@Composable
private fun ApplianceCard(
    appliance: ApplianceEntity,
    uiFlags: ApplianceUiFlags,
    onRowClick: () -> Unit,
    onPowerToggle: (enabled: Boolean) -> Unit
) {
    val isOnline = (appliance.online ?: 0L) == 1L && (appliance.activity ?: 0L) == 1L

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        // Header row (image, title, subtitle, chevron)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onRowClick)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SafeImage(
                imageBitmap = LoadImageBitmap(AppImage.ModecoIcon),
                imageModifier = Modifier.size(48.dp),
                contentScale = ContentScale.Fit,
                contentDescription = AppImage.ModecoIcon.contentDescription,
                fallbackContent = {}
            )

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = appliance.deviceSerial,
                    style = MaterialTheme.typography.titleSmall
                )
                val modelLabel = appliance.description
                    ?.takeIf { it.isNotBlank() }
                    ?: appliance.shortName?.takeIf { it.isNotBlank() }
                    ?: "Modeco II" // TODO: Appliances endpoint does not return this string. Find from where can you get it.
                if (modelLabel.isNotEmpty()) {
                    Text(
                        text = modelLabel,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            if (isOnline) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null
                )
            }
        }

        AnimatedVisibility(
            visible = uiFlags.errorMessage != null,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            HorizontalDivider()

            Text(
                modifier = Modifier.padding(16.dp),
                text = uiFlags.errorMessage.orEmpty(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error
            )
        }

        HorizontalDivider()

        if (isOnline) {
            ApplianceOnlineContent(
                appliance = appliance,
                uiFlags = uiFlags,
                onPowerToggle = onPowerToggle
            )
        } else {
            ApplianceOfflineContent()
        }
    }
}

// ---------- ONLINE CONTENT ----------

@Composable
private fun ApplianceOnlineContent(
    appliance: ApplianceEntity,
    uiFlags: ApplianceUiFlags,
    onPowerToggle: (enabled: Boolean) -> Unit
) {
    val currentTemp = appliance.statusTmpC?.toDoubleOrNull()
    val powerOn = (appliance.statusPwr == "1")
    val modeLabel = modeLabelFrom(appliance)
    val statusText = (appliance.statusText ?: "").ifBlank { "--" }
    val isHeating = statusText.equals("heating", true)
    val targetTempLabel =
        appliance.statusTmpR?.toDoubleOrNull()?.let { "${it.toInt()}°C" } ?: "--°C"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
    ) {
        // LEFT: target chip, temperature circle + power button
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //TargetChip(targetTemp)

            Box(
                modifier = Modifier.padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                TemperatureCircle(
                    temp = currentTemp,
                    isPoweredOn = powerOn,
                    isHeating = isHeating
                )

                // Overlapping circular power button
                FloatingActionButton(
                    onClick = { onPowerToggle(powerOn) },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(44.dp),
                    shape = CircleShape,
                    containerColor = Color.White
                ) {
                    if (uiFlags.showPowerButtonSpinner) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            strokeWidth = 2.dp,
                            color = Color(0xFF5E006C)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.PowerSettingsNew,
                            contentDescription = if (powerOn) "Turn off" else "Turn on",
                            tint = if (powerOn) Color(0xFFeb445a) else Color(0xFF3CAF3D)
                        )
                    }
                }
            }
        }

        // RIGHT: details (chip + three labeled rows)
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Rows
            LabeledValueRow(
                label = "APPLIANCE",
                icon = Icons.Default.PowerSettingsNew,
                value = if (powerOn) "Active" else "Inactive",
                valueColor = if (powerOn) Color(0xFF3CAF3D) else Color(0xFFeb445a)
            )

            LabeledValueRow(
                label = "CURRENT MODE",
                icon = Icons.Default.TouchApp,
                value = modeLabel,
                valueColor = Color(0xFF3CAF3D),
                enabled = powerOn
            )

            LabeledValueRow(
                label = "STATUS",
                icon = Icons.Default.Thermostat,
                value = statusText,
                valueColor = if (isHeating) Color(0xFFeb445a) else Color(0xFF3CAF3D),
                enabled = powerOn
            )

            LabeledValueRow(
                label = "TARGET",
                icon = Icons.Default.Thermostat,
                value = targetTempLabel,
                valueColor = Color(0xFF3CAF3D),
                enabled = powerOn
            )
        }
    }
}

@Composable
private fun TemperatureCircle(
    temp: Double?,
    isPoweredOn: Boolean,
    isHeating: Boolean
) {
    val display = temp?.let { "${(it * 10).roundToInt() / 10.0}°C" } ?: "--"
    val displayColor = when {
        isHeating -> Color(0xFFeb445a)
        isPoweredOn -> Color(0xFF3CAF3D)
        else -> Color(0xFF9f9f9f)
    }

    Box(
        modifier = Modifier
            .size(150.dp)
            .border(
                width = 1.dp,
                color = displayColor,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = display,
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = displayColor
                )
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = "CURRENT",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = displayColor
                )
            )
        }
    }
}

// TODO Show/Hide based on powered on setting
@Composable
private fun TargetChip(targetTemp: Double?) {
    val label = targetTemp?.let { "TARGET ${it.toInt()}°C" } ?: "TARGET --°C"
    Surface(
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, Color(0xFF3CAF3D)),
        color = Color.Transparent
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelMedium.copy(
                color = Color(0xFF3CAF3D)
            )
        )
    }
}

@Composable
private fun LabeledValueRow(
    label: String,
    icon: ImageVector,
    value: String,
    valueColor: Color,
    enabled: Boolean = true // Track if appliance if powered on or off
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color =
                    if (enabled) MaterialTheme.colorScheme.onSurfaceVariant
                    else Color(0xFF9f9f9f)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = if (enabled) valueColor else Color(0xFF9f9f9f)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (enabled) valueColor else Color(0xFF9f9f9f)
                )
            }
        }
    }
}

// ---------- OFFLINE CONTENT ----------

@Composable
private fun ApplianceOfflineContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Simple offline icon & dot
        Icon(
            imageVector = Icons.Default.WifiOff,
            contentDescription = null,
            modifier = Modifier.size(60.dp),
            tint = Color(0xFFeb445a)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "The appliance is currently",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "Offline",
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFFeb445a)
        )
    }
}

// ---------- HELPERS ----------

private fun modeLabelFrom(appliance: ApplianceEntity): String {
    // Best-effort label from available fields.
    // If you later have a strict mapping, plug it here.
    return when (appliance.statusMode) {
        "0" -> "Manual"
        "1" -> "Program P1"
        "2" -> "Program P2"
        "3" -> "Program P3"
        else -> "Undefined"
    }
}