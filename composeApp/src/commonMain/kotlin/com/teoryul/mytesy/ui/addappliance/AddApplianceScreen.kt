package com.teoryul.mytesy.ui.addappliance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.teoryul.mytesy.ui.common.AppImage
import com.teoryul.mytesy.ui.common.BulletList
import com.teoryul.mytesy.ui.common.LoadImageBitmap
import com.teoryul.mytesy.ui.common.SafeImage

@Composable
fun AddApplianceScreen(
    stateHolderKey: String,
    onNextClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.Top)
    ) {
        SafeImage(
            imageBitmap = LoadImageBitmap(AppImage.AddApplianceImage),
            imageModifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Fit,
            contentDescription = AppImage.AddApplianceImage.contentDescription,
            fallbackContent = {}
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "To start, you'll need:",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color(0xFF5e006c)
                )
            )

            Spacer(Modifier.height(12.dp))

            BulletList(
                items = listOf(
                    "To be close to the appliance.",
                    "To switch on only one TESY appliance that has not been previously connected.",
                    "To know the name and password of your 2.4 GHz Wi-Fi network."
                )
            )
        }

        Button(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 18.dp)
                .fillMaxWidth()
                .height(54.dp),
            onClick = onNextClick,
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF5E006C),
                contentColor = Color.White
            )
        ) {
            Text("Next", color = Color.White)
        }
    }
}