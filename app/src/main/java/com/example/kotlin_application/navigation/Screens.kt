package com.example.kotlin_application.navigation


enum class Screens {
    SplashScreen,
    GetStartedScreen,
    LoginScreen,
    CreateAccountScreen;

    companion object {
        fun fromRoute (route : String): Screens
                = when (route?.substringBefore("/")) {
            GetStartedScreen.name -> GetStartedScreen
            SplashScreen.name -> SplashScreen
            LoginScreen.name -> LoginScreen
            CreateAccountScreen.name -> CreateAccountScreen
            else -> throw java.lang.IllegalArgumentException("Route $route is not recognized")
        }
    }
}