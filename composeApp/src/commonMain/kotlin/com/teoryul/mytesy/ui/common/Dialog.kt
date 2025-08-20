package com.teoryul.mytesy.ui.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun AlertDialog(
    show: Boolean,
    title: String? = null,
    message: String,
    buttonText: String,
    onDismiss: () -> Unit
) {
    if (!show) return
    AlertDialog(
        onDismissRequest = onDismiss,
        title = title?.let { { Text(it, style = MaterialTheme.typography.titleMedium) } },
        text = { Text(message, style = MaterialTheme.typography.bodyMedium) },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text(buttonText) }
        }
    )
}