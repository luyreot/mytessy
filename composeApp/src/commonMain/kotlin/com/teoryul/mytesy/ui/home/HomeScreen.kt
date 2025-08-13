package com.teoryul.mytesy.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    stateHolderKey: String,
    viewModel: HomeViewModel = koinViewModel(),
    onAddApplianceClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        EmptyAppliancePlaceholder(
            onAddApplianceClick = onAddApplianceClick
        )
    }
}

@Composable
private fun EmptyAppliancePlaceholder(
    modifier: Modifier = Modifier,
    onAddApplianceClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "APPLIANCE",
            style = MaterialTheme.typography.labelSmall.copy(
                color = Color.Gray,
                fontSize = 12.sp,
                letterSpacing = 1.5.sp
            )
        )

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = "You have no connected appliances",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedButton(
            modifier = Modifier.size(56.dp),
            onClick = onAddApplianceClick,
            shape = CircleShape,
            border = BorderStroke(1.dp, Color(0xFF5E006C)),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text("+", color = Color(0xFF5E006C), fontSize = 24.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Add appliance",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color(0xFF5E006C),
                fontWeight = FontWeight.SemiBold
            )
        )

        Text(
            text = "Connect your smart TESY appliance",
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray),
            textAlign = TextAlign.Center
        )
    }
}