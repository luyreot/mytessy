package com.teoryul.mytesy.ui.comingsoon

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.teoryul.mytesy.ui.common.ComingSoon

@Composable
fun ComingSoonScreen(
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        ComingSoon()
    }
}