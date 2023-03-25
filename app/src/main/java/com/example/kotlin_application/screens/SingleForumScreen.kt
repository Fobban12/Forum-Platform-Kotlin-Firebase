package com.example.kotlin_application.screens

import android.util.Log
import android.widget.GridLayout.Alignment
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.kotlin_application.data.Forum
import com.example.kotlin_application.navigation.Screens
import com.example.kotlin_application.viewmodel.ForumViewModel
import com.google.firebase.firestore.QuerySnapshot

@ExperimentalComposeUiApi
@Composable
fun SingleForumScreen (navController: NavController, forumId: String, viewModel: ForumViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {

    //Test with single forum screen based on dummy data

    //Set image height based on screen height
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val imageHeight = with(LocalDensity.current) { screenHeight * 0.3f }

    //Set effect to fetch single forum screen
    LaunchedEffect(forumId, viewModel) {
        viewModel.getSingleForum(forumId);
    }

    //Single forum data
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
                contentColor = Color.White
            )
        },
        floatingActionButton = {
            renderFloatingButtonActionToAddComment(navController)
        }

    ) {
        it
        Surface (
            modifier = Modifier.fillMaxSize()
        ) {
            Column (modifier = Modifier.fillMaxWidth()){
                val painterState = rememberImagePainter(
                    data = "${singleForum.value?.image}",
                    builder = {})
                Spacer(modifier = Modifier.height(20.dp))
                Image(
                    painter = painterState,
                    contentDescription = "Image for forum",
                    modifier = Modifier
                        .height(imageHeight)
                        .fillMaxWidth(),
                    contentScale = ContentScale.FillWidth,
                )
                Spacer(modifier = Modifier.height(5.dp))
                
                
                Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(corner = CornerSize(8.dp)), border = BorderStroke(width = 2.dp, color = Color.LightGray)) {
                    Column(modifier = Modifier
                        .fillMaxWidth()) {
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .background(color = MaterialTheme.colors.onBackground)
                            .clip(
                                RoundedCornerShape(16.dp)
                            )) {
                            Column() {
                                Text(text = "Title of forum: ${singleForum.value?.title}", style = TextStyle(color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp), modifier = Modifier.padding(10.dp))
                                Text(text = "Description: ${singleForum.value?.description}", style = TextStyle(color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp), modifier = Modifier.padding(10.dp))
                                Text(text = "Created by: ${singleForum.value?.username}", style = TextStyle(color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp), modifier = Modifier.padding(10.dp))

                            }

                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = "Comments: ", style = TextStyle(color = MaterialTheme.colors.onBackground, fontWeight = FontWeight.Bold, fontSize = 18.sp), modifier = Modifier.padding(3.dp))



            }
        }
    }



//        AlertDialog(
//            onDismissRequest = {
//                               showDialog.value = !showDialog.value
//            },
//            modifier = Modifier.height(commentBoxHeight),
//            title = {
//                Text(text = "Dialog title")
//            },
//            text = {
//                OutlinedTextField(value = comment.value, onValueChange = {comment.value = it}, label = { Text(
//                    text = "Comment"
//                )}, enabled = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done), keyboardActions = KeyboardActions(onDone = {
//                    if (!commentIsValid) return@KeyboardActions;
//                    keyboardController.clearFocus();
//                    Log.d("Comment value: ", "${comment.value}")
//                }), singleLine = false, )
//            },
//            confirmButton = {
//                Button(
//                    onClick = {
//
//                        if (!commentIsValid || !commentLengthIsValid) {
//                            Toast.makeText(context, "Comment is not valid as you should add a comment with minimun length of 5 characters", Toast.LENGTH_LONG).show()
//                            keyboardController.clearFocus()
//                        } else {
//                            showDialog.value = !showDialog.value
//                            keyboardController.clearFocus()
//                        }
//                        keyboardController.clearFocus()
//
//
//                    },
//                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.onBackground)
//                ) {
//                    Text(text = "OK", style = TextStyle(color = MaterialTheme.colors.onSecondary))
//                }
//            },
//            dismissButton = {
//                Button(
//                    onClick = {
//                        showDialog.value = !showDialog.value
//                    },
//                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.onBackground)
//                ) {
//                    Text(text = "Cancel", style = TextStyle(color = MaterialTheme.colors.onSecondary))
//                }
//            }
//        )



}



@Composable
fun renderFloatingButtonActionToAddComment (navController: NavController) {


    FloatingActionButton(onClick = { navController.navigate(Screens.AddCommentScreen.name) }, shape = CircleShape, backgroundColor = MaterialTheme.colors.onBackground) {
        Text(text = "Add New Comment", modifier = Modifier.padding(10.dp),style = TextStyle(color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold), textAlign = TextAlign.Center)
    }
}