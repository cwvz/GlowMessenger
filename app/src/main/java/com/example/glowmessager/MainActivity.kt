package com.example.glowmessager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.glowmessager.ui.chat.ChatScreen
import com.example.glowmessager.ui.chat.EmptyDetailPaneState
import com.example.glowmessager.ui.conversation.ConversationListScreen
import com.example.glowmessager.ui.theme.GlowMessagerTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GlowMessagerTheme {
                GlowMessagerApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun GlowMessagerApp() {
    val navigator = rememberListDetailPaneScaffoldNavigator<String>()
    val coroutineScope = rememberCoroutineScope()

    val currentSelectedId = navigator.currentDestination?.contentKey

    NavigableListDetailPaneScaffold(
        navigator = navigator,
        listPane = {
            AnimatedPane {
                ConversationListScreen(
                    selectedConversationId = currentSelectedId,
                    onConversationClick = { id ->
                        coroutineScope.launch {
                            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, id)
                        }
                    }
                )
            }
        },
        detailPane = {
            AnimatedPane {
                if (currentSelectedId != null) {
                    ChatScreen(
                        conversationId = currentSelectedId,
                        onBackClick = {
                            coroutineScope.launch {
                                if (navigator.canNavigateBack()) {
                                    navigator.navigateBack()
                                } else {
                                    navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, null)
                                }
                            }
                        }
                    )
                } else {
                    EmptyDetailPaneState()
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}
