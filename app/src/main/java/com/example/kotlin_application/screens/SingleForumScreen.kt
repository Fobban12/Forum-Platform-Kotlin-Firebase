package com.example.kotlin_application.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.kotlin_application.data.Forum
import com.example.kotlin_application.navigation.Screens
import com.example.kotlin_application.viewmodel.ForumViewModel
import com.google.firebase.firestore.QuerySnapshot

@ExperimentalComposeUiApi
@Composable
fun SingleForumScreen (navController: NavController, forumId: String, viewModel: ForumViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {

    val rememberedForum = remember {
        mutableStateOf<Forum?>(null)
    }
    
    LaunchedEffect(forumId, viewModel) {
        viewModel.getSingleForum(forumId);
    }
    
    val singleForum = viewModel.singleForum;
    
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screens.MainScreen.name
                    ) }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back Button")
                    }
                },
                title = {
                    Text(text = "Forum Name: ${singleForum.value?.title}")
                },
                backgroundColor = MaterialTheme.colors.onBackground,
                contentColor = MaterialTheme.colors.onSecondary
            )
        },

    ) {
        it
        Surface (
            modifier = Modifier.fillMaxSize()
        ) {
            Column (modifier = Modifier.fillMaxWidth()){
                Text(text = "Single Forum")

                Text(text = "${singleForum.value?.title}")


            }
        }
    }


}