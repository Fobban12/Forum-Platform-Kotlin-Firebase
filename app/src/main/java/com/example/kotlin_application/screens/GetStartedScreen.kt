package com.example.kotlin_application.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.kotlin_application.navigation.Screens
import java.time.format.TextStyle

@Composable
fun GetStartedScreen(navController: NavController) {
    Button(
        onClick = {
            navController.navigate(
                Screens.LoginScreen.name
            )
        },
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "Get Started",
            style = androidx.compose.ui.text.TextStyle(fontWeight = FontWeight.Bold)
        )
    }
}