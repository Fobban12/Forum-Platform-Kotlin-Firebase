package com.example.kotlin_application.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.kotlin_application.navigation.Screens
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@Composable
fun ForumScreen(navController: NavController)
{
    Scaffold(
        topBar = {
            renderTop(
                navController,
                IconClick = {
                    navController.navigate(Screens.MainScreen.name)
                    })
                }
            ){it}
  
}


@ExperimentalComposeUiApi
@Composable
fun renderTop(navController: NavController, IconClick: () -> Unit)
{
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = IconClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Go Back"
                )
            }
        },
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                    Text(text = "Forum Post, Change the name later")
            }
        },
        backgroundColor = MaterialTheme.colors.onBackground,
        contentColor = MaterialTheme.colors.onSecondary
    )

}