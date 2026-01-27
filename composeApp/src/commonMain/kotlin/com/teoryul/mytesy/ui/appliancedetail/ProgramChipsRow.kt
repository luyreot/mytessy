package com.teoryul.mytesy.ui.appliancedetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.teoryul.mytesy.ui.helper.ApplianceProgramMode

@Composable
fun ProgramChipsRow(
    modifier: Modifier = Modifier,
    active: ApplianceProgramMode,
    selected: ApplianceProgramMode,
    onSelect: (ApplianceProgramMode) -> Unit
) {
    Row(
        modifier = modifier.padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProgramChipItem(
            label = ApplianceProgramMode.Manual.labelShort,
            active = active == ApplianceProgramMode.Manual,
            selected = selected == ApplianceProgramMode.Manual,
            onClick = { onSelect(ApplianceProgramMode.Manual) }
        )
        ProgramChipItem(
            label = ApplianceProgramMode.P1.labelShort,
            active = active == ApplianceProgramMode.P1,
            selected = selected == ApplianceProgramMode.P1,
            onClick = { onSelect(ApplianceProgramMode.P1) }
        )
        ProgramChipItem(
            label = ApplianceProgramMode.P2.labelShort,
            active = active == ApplianceProgramMode.P2,
            selected = selected == ApplianceProgramMode.P2,
            onClick = { onSelect(ApplianceProgramMode.P2) }
        )
        ProgramChipItem(
            label = ApplianceProgramMode.P3.labelShort,
            active = active == ApplianceProgramMode.P3,
            selected = selected == ApplianceProgramMode.P3,
            onClick = { onSelect(ApplianceProgramMode.P3) }
        )
    }
}

@Composable
fun ProgramChipItem(
    label: String,
    active: Boolean,
    selected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            if (active) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = label)
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(
                        modifier = Modifier.size(14.dp),
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Active program",
                        tint = Color(0xFF8DC63F)
                    )
                }
            } else {
                Text(text = label)
            }
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Color.Transparent,
            selectedLabelColor = Color(0xFF8DC63F),
            containerColor = Color.Transparent,
            labelColor = if (active) Color(0xFF8DC63F) else Color.Gray
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selected,
            borderColor = Color.Transparent,
            selectedBorderColor = Color(0xFF8DC63F),
            borderWidth = 1.dp,
            selectedBorderWidth = 1.dp
        )
    )
}