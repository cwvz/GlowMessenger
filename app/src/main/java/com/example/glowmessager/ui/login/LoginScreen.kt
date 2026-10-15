package com.example.glowmessager.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.glowmessager.ui.theme.AmoledBlack
import com.example.glowmessager.ui.theme.AmoledSurface
import com.example.glowmessager.ui.theme.AmoledSurfaceVariant
import com.example.glowmessager.ui.theme.TextMuted
import com.example.glowmessager.ui.theme.TextPrimary
import com.example.glowmessager.ui.theme.TextSecondary

@Composable
fun LoginScreen(
    onLoginSuccess: (String) -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AmoledBlack)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "GlowMessenger",
                color = TextPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Clean AMOLED Edition",
                color = TextSecondary,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it; errorMessage = "" },
                label = { Text("Username or Email", color = TextSecondary) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TextPrimary,
                    unfocusedBorderColor = AmoledSurfaceVariant,
                    cursorColor = TextPrimary,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedContainerColor = AmoledSurface,
                    unfocusedContainerColor = AmoledSurface
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it; errorMessage = "" },
                label = { Text("Password", color = TextSecondary) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TextPrimary,
                    unfocusedBorderColor = AmoledSurfaceVariant,
                    cursorColor = TextPrimary,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedContainerColor = AmoledSurface,
                    unfocusedContainerColor = AmoledSurface
                ),
                shape = RoundedCornerShape(12.dp)
            )

            if (errorMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (username.isBlank()) {
                        errorMessage = "Please enter a username"
                    } else {
                        onLoginSuccess(username.trim())
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TextPrimary,
                    contentColor = AmoledBlack
                )
            ) {
                Text(
                    text = "Login",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Online sync mode ready (offline currently)",
                color = TextMuted,
                fontSize = 12.sp
            )
        }
    }
}
