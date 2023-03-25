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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            
                        Column() {
                            Text(text = "${singleItem?.comment}", style = TextStyle(color = MaterialTheme.colors.onSecondary, fontWeight = FontWeight.Bold, fontSize = 16.sp))
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(text = if (singleItem?.userId == uid) "Created by: You" else "Created by: ${singleItem?.username}", style = TextStyle(color = MaterialTheme.colors.onSecondary, fontWeight = FontWeight.Bold, fontSize = 16.sp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        if (singleItem?.userId == uid) Row(modifier = Modifier.border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(8.dp))) {
                            IconButton(onClick = { /*TODO*/ }, modifier = Modifier.background(color = MaterialTheme.colors.onSurface)) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete comment")
                            }
                        }
                    }
                }
            }
        }
    }


}