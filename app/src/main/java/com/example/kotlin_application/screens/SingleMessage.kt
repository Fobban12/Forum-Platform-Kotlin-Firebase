package com.example.kotlin_application.screens

import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun SingleMessage (messageId : String) {

    Text(text = "${messageId}");
}