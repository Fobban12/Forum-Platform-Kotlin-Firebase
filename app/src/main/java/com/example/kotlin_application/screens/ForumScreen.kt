package com.example.kotlin_application.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.kotlin_application.navigation.Screens

@ExperimentalComposeUiApi
@Composable
fun ForumScreen(navController: NavController){
    Box(
    contentAlignment = Alignment.Center){
    Text(text = "Start for the Forum testing") }

}