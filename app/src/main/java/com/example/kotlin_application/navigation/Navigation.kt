package com.example.kotlin_application.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kotlin_application.screens.GetStartedScreen
import com.example.kotlin_application.screens.MainScreen
import com.example.kotlin_application.screens.authentication.LoginScreen

@ExperimentalComposeUiApi
@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screens.GetStartedScreen.name) {
        composable(Screens.GetStartedScreen.name) {
            GetStartedScreen(navController = navController)
        }

        composable(Screens.LoginScreen.name) {
            LoginScreen(navController = navController)
        }

        composable(Screens.MainScreen.name) {
            MainScreen(navController = navController)
        }
    }
}