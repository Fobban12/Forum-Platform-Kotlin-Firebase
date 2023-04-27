package com.example.kotlin_application.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.kotlin_application.viewmodel.CommentViewModel

@ExperimentalComposeUiApi
@Composable
fun CommentScreen (navController: NavController, forumId : String) {

    //Get view Model
   val commentViewModel: CommentViewModel = viewModel()

    //Set effect to fetch list of comments
    LaunchedEffect(forumId, commentViewModel) {
        commentViewModel.fetchComments(forumId);
    }


    //List of comments of forum
    val comments = commentViewModel.comments;


    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back Button")
                    }
                },
                title = {
                    Text(text = "Comments")
                },
                backgroundColor = MaterialTheme.colors.onBackground,
                contentColor = Color.White
            )
        }
    ) {
        it

        if (comments.size == 0) {
            Surface(modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
                .fillMaxSize()) {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Text(text = "There are no comments for this forum", style = TextStyle(fontWeight = FontWeight.Bold, color = MaterialTheme.colors.onBackground, fontSize = 20.sp))
                }
            }
        } else {
            LazyColumn(modifier = Modifier.padding(2.dp)) {
                items(comments) {
                        singleItem ->
                    SingleCommentScreen(singleItem = singleItem, forumId = forumId, navController = navController)
                }
            }
        }

    }


}