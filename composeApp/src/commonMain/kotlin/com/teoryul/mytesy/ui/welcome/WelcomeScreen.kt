package com.teoryul.mytesy.ui.welcome

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teoryul.mytesy.ui.common.RunOnce
import com.teoryul.mytesy.ui.common.SafeImage
import com.teoryul.mytesy.ui.common.AppImage
import com.teoryul.mytesy.ui.common.LoadImageBitmap
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WelcomeScreen(
    viewModel: WelcomeViewModel = koinViewModel(),
    onSignUpClick: () -> Unit,
    onSignInClick: () -> Unit,
    onLanguageClick: () -> Unit,
    onSessionRestored: () -> Unit
) {
    RunOnce("restoreSession") {
        viewModel.restoreSession()
    }

    val viewState by viewModel.viewState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.viewEffect.collect { effect ->
            when (effect) {
                is WelcomeViewEffect.NavigateToHome -> onSessionRestored()
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SafeImage(
                imageBitmap = LoadImageBitmap(AppImage.WelcomeLogo),
                imageModifier = Modifier
                    .size(144.dp, 44.dp),
                contentScale = ContentScale.Fit,
                contentDescription = AppImage.WelcomeLogo.contentDescription,
                fallbackContent = {
                    Column {
                        Text(
                            text = "TESY",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = Color(0xFF5e006c),
                                fontWeight = FontWeight.Bold,
                                fontSize = 36.sp
                            )
                        )
                        Text(
                            text = "It’s impressive",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF3CAF3D),
                                fontSize = 12.sp
                            )
                        )
                    }
                }
            )

            if (!viewState.isLoading) {
                OutlinedButton(
                    onClick = onLanguageClick,
                    shape = CircleShape,
                    border = BorderStroke(1.dp, Color(0xFFDDDDDD)),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.size(40.dp)
                ) {
                    Text("EN")
                }
            }
        }
        if (viewState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color(0xFF5E006C),
                    strokeWidth = 4.dp
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SafeImage(
                    imageBitmap = LoadImageBitmap(AppImage.WelcomeImage),
                    imageModifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray),
                    contentScale = ContentScale.Crop,
                    contentDescription = AppImage.WelcomeImage.contentDescription,
                    fallbackBoxModifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Welcome to MyTESY",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = Color(0xFF5e006c),
                        fontWeight = FontWeight.Medium
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Keep your home close to you",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.Gray
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(48.dp))

                Button(
                    onClick = onSignUpClick,
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF5E006C),
                        contentColor = Color.White
                    )
                ) {
                    Text("Sign up in MyTESY")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = buildAnnotatedString {
                        append("Already a member? ")
                        pushStringAnnotation(tag = "sign_in", annotation = "sign_in")
                        withStyle(
                            SpanStyle(
                                color = Color(0xFF5e006c),
                                fontWeight = FontWeight.SemiBold
                            )
                        ) {
                            append("Sign in")
                        }
                        pop()
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.clickable { onSignInClick() },
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}