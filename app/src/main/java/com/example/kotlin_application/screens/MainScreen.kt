package com.example.kotlin_application.screens

import android.util.Log
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.example.kotlin_application.navigation.Screens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

@Composable
fun MainScreen (navController: NavController) {

    Text(text = "Home Screen")

    LaunchedEffect(key1 = true) {
        if (FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()) {
            navController.navigate(Screens.LoginScreen.name)
        } else {
            navController.navigate(Screens.MainScreen.name)
            Log.d("Firebase", FirebaseAuth.getInstance().currentUser?.email.toString());
        }
    }
}