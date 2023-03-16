package com.example.kotlin_application.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.kotlin_application.navigation.Screens


@Composable
fun GetStartedScreen (navController: NavController) {

    Button(onClick = { navController.navigate(Screens.LoginScreen.name
    ) }, modifier = Modifier
        .padding(10.dp)
        .fillMaxWidth()) {
        Text(text = "Get Started")
    }
}