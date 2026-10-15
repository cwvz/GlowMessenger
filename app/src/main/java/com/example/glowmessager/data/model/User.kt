package com.example.glowmessager.data.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val name: String,
    val avatarUrl: String? = null,
    val isOnline: Boolean = false,
    val statusMessage: String? = null,
    val neonColorHex: String? = null
)
