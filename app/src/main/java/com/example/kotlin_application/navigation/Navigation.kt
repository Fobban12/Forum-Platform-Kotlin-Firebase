package com.example.kotlin_application.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kotlin_application.screens.*
import com.example.kotlin_application.screens.authentication.LoginScreen

@ExperimentalComposeUiApi
@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screens.MainScreen.name) {
        composable(Screens.LoginScreen.name) {
            LoginScreen(navController = navController)
        }
        composable(Screens.ForumScreen.name) {
            ForumScreen(navController = navController)
        }

        composable(Screens.MainScreen.name) {
            MainScreen(navController = navController)
        }
        composable(Screens.ChatScreen.name) {
            ChatScreen(navController = navController)
        }
        composable(Screens.SettingScreen.name) {
            SettingScreen(navController = navController)
        }
        composable(Screens.ForumPost.name) {
            ForumPost(navController = navController)
        }

        composable(Screens.SingleForumScreen.name) {

        }

    }
}