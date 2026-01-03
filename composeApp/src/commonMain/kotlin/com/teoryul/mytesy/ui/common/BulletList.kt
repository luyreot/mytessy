package com.teoryul.mytesy.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BulletList(
    items: List<String>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items.forEach { text ->
            Row(verticalAlignment = Alignment.Top) {
                Text(
                    modifier = Modifier.padding(end = 10.dp),
                    text = "•",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    modifier = Modifier.weight(1f),
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,
                    lineHeight = 22.sp
                )
            }
        }
    }
}
