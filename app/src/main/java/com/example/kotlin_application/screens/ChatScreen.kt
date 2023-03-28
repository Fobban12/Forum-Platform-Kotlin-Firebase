package com.example.kotlin_application.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.kotlin_application.navigation.Screens

@ExperimentalComposeUiApi
@Composable
fun ChatScreen(navController: NavController) {



    Scaffold(
        topBar = {
            ChatScreenTopBar(
                navController,
                IconClick = {
                    navController.navigate(Screens.MainScreen.name)
                })
        }
    ){it}
}

@ExperimentalComposeUiApi
@Composable
fun ChatScreenTopBar(navController: NavController, IconClick: () -> Unit)
{
    val scaffoldState = rememberScaffoldState();

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
                Text(text = "Chat Screen")
            }
        },
        backgroundColor = MaterialTheme.colors.onBackground,
        contentColor = MaterialTheme.colors.onSecondary
    )







}