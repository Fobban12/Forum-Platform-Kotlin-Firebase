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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.kotlin_application.navigation.Screens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import java.time.format.TextStyle

@ExperimentalComposeUiApi
@Composable
fun MainScreen (navController: NavController) {

//
//    LaunchedEffect(key1 = true) {
//        if (FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()) {
//            navController.navigate(Screens.LoginScreen.name)
//        } else {
//            navController.navigate(Screens.MainScreen.name)
//            Log.d("Firebase", FirebaseAuth.getInstance().currentUser?.email.toString());
//        }
//    }

    val checkUserIsNull = remember (FirebaseAuth.getInstance().currentUser?.email) {
        FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty();
    };

    val username = FirebaseAuth.getInstance().currentUser?.email?.split("@")?.get(0);
    
    Scaffold(topBar = { renderTopAppBar(username, checkUserIsNull, navController)}) {
        it
    }
}

@ExperimentalComposeUiApi
@Composable
fun renderTopAppBar (
    username: String?,
    checkUserIsNull: Boolean,
    navController: NavController
) {
    TopAppBar(title = { Row(modifier = Modifier
        .fillMaxWidth()) {
        if (!checkUserIsNull) {
            Text(text = "Hello ${username}")
        } else {
            Text(text = "")
        }
    }}, actions = {

        if (!checkUserIsNull) {
            IconButton(onClick = { FirebaseAuth.getInstance().signOut().run { navController.navigate(Screens.LoginScreen.name) } }) {
                Icon(imageVector = Icons.Filled.Logout, contentDescription = "Log Out")
            }
        }

    })
}




