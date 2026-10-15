package com.example.glowmessager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.example.glowmessager.ui.login.LoginScreen
import com.example.glowmessager.ui.main.MainScreen
import com.example.glowmessager.ui.theme.GlowMessagerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GlowMessagerTheme {
                var isLoggedIn by remember { mutableStateOf(false) }
                var username by remember { mutableStateOf("") }

                if (!isLoggedIn) {
                    LoginScreen(
                        onLoginSuccess = { name ->
                            username = name
                            isLoggedIn = true
                        }
                    )
                } else {
                    MainScreen(
                        username = username,
                        onLogout = {
                            isLoggedIn = false
                            username = ""
                        }
                    )
                }
            }
        }
    }
}
