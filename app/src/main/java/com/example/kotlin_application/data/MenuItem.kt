package com.example.kotlin_application.data

import androidx.compose.ui.graphics.vector.ImageVector

data class MenuItem(
    val id: String?= null,
    val title: String?= null,
    val contentDescription: String?= null,
    val icon: ImageVector? = null
)
