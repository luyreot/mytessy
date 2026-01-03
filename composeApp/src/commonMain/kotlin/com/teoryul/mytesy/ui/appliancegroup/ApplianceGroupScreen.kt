package com.teoryul.mytesy.ui.appliancegroup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.teoryul.mytesy.domain.usecase.ApplianceGroupItem
import com.teoryul.mytesy.ui.common.ApplianceCard

@Composable
fun ApplianceGroupScreen(
    stateHolderKey: String,
    items: List<ApplianceGroupItem>,
    onItemClick: (ApplianceGroupItem) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
        ) {
            items(
                items = items,
                key = { it.hashCode() }
            ) { item ->
                ApplianceCard(
                    name = item.name,
                    image = item.icon,
                    onApplianceClicked = { onItemClick(item) }
                )
            }
        }
    }
}