package com.example.kotlin_application.navigation

import com.example.kotlin_application.screens.ForumScreen

enum class Screens {
    LoginScreen,
    MainScreen,
    ChatScreen,
    SettingScreen,
    ForumScreen;

    companion object {
        fun fromRoute(route: String): Screens =
            when (route?.substringBefore("/")) {
                LoginScreen.name -> LoginScreen
                MainScreen.name -> MainScreen
                ChatScreen.name -> ChatScreen
                SettingScreen.name -> SettingScreen
                ForumScreen.name -> ForumScreen

                else -> throw java.lang.IllegalArgumentException("Route $route is not recognized")
            }
    }
}