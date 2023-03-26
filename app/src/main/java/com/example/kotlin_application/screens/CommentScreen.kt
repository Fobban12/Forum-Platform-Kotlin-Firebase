package com.example.kotlin_application.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.kotlin_application.navigation.Screens
import com.example.kotlin_application.viewmodel.CommentViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun CommentScreen (navController: NavController, forumId : String, commentViewModel: CommentViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    //Set effect to fetch list of comments
    LaunchedEffect(forumId, commentViewModel) {
        commentViewModel.fetchComments(forumId);
    }

    //Get uid from firebase
    val uid = FirebaseAuth.getInstance()?.uid;

    //List of comments of forum
    val comments = commentViewModel.comments;

    //Set context for Toast
    val context = LocalContext.current;

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
                .fillMaxWidth().fillMaxSize()) {
                Column(modifier = Modifier.fillMaxWidth().fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Text(text = "There are no comments for this forum", style = TextStyle(fontWeight = FontWeight.Bold, color = MaterialTheme.colors.onBackground, fontSize = 20.sp))
                }
            }
        } else {
            LazyColumn(modifier = Modifier.padding(2.dp)) {
                items(comments) {
                        singleItem ->

                    Box(modifier = Modifier
                        .padding(4.dp)
                        .border(
                            width = 2.dp,
                            color = Color.Black,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .background(MaterialTheme.colors.onSurface)
                        .padding(20.dp)
                        .fillMaxWidth()) {
                        Column(modifier = Modifier.fillMaxWidth()) {

                            Column() {
                                Text(text = "Comment Content: ${singleItem?.comment}", style = TextStyle(color = MaterialTheme.colors.onSecondary, fontWeight = FontWeight.Bold, fontSize = 16.sp), modifier = Modifier.fillMaxWidth())
                                Spacer(modifier = Modifier.height(5.dp))
                                Text(text = if (singleItem?.userId == uid) "Created by: You" else "Created by: ${singleItem?.username}", style = TextStyle(color = MaterialTheme.colors.onSecondary, fontWeight = FontWeight.Bold, fontSize = 16.sp))
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            if (singleItem?.userId == uid) Row(modifier = Modifier.border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(8.dp))) {
                                IconButton(onClick = { commentViewModel.deleteComment(singleItem?.id as String, context) },modifier = Modifier
                                    .background(color = MaterialTheme.colors.onBackground)
                                    .fillMaxWidth()) {
                                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete comment", tint = Color.White)
                                }
                            }
                        }
                    }
                }
            }
        }

    }


}