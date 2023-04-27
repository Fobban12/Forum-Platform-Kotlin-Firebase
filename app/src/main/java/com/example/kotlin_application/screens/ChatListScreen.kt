package com.example.kotlin_application.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.kotlin_application.navigation.Screens
import com.example.kotlin_application.viewmodel.ChatVIewModel
import com.google.firebase.auth.FirebaseAuth


@Composable
fun ChatListScreen (navController: NavController) {

    //Get uid from chat list screen
    val uid = FirebaseAuth.getInstance().uid.toString();

    //Get view model for chat
    val chatVIewModel : ChatVIewModel = viewModel();

    //Check user is logged in or not
    val checkUserIsNull = remember(FirebaseAuth.getInstance().currentUser?.email) {
        FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()
    };

    //Set effect to check user logged in to access this screen
    LaunchedEffect(checkUserIsNull) {
        if (checkUserIsNull) {
            navController.navigate(Screens.MainScreen.name)
        }
    }

    //Set effect to fetch all chats where user participates
    LaunchedEffect(uid) {
        chatVIewModel.fetchAllChatsByUserId(uid);
    }
    
    //Get state for all chats of user
    val allChats = chatVIewModel.allSingleChatsByUserId;


    val listState = rememberLazyListState()




    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack()}) {
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
                        Text(text = "Chat List Screen")
                    }
                },
                backgroundColor = MaterialTheme.colors.onBackground,
                contentColor = MaterialTheme.colors.onSecondary
            )
        },

    ) {
        it
        Surface(modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 20.dp, horizontal = 10.dp)) {
            Column(modifier = Modifier.fillMaxSize()) {


                LazyColumn(
                    state = listState,
                ) {
                    items(allChats) {
                        item ->
                        SingleMessageBox(navController = navController, item = item)
                    }






                }
            }

        }
    }
}