package com.example.kotlin_application.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.kotlin_application.navigation.Screens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import java.time.format.TextStyle

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

    val checkUserValid = remember (FirebaseAuth.getInstance().currentUser?.email) {
        FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty();
    };

    val username = FirebaseAuth.getInstance().currentUser?.email?.split("@")?.get(0);

    Text(text = if (!checkUserValid) "Hello ${username}" else "")

}

