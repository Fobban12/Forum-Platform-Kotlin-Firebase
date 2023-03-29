package com.example.kotlin_application.screens

import android.util.Log
import android.widget.GridLayout.Alignment
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
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
import androidx.compose.ui.res.painterResource
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
import com.example.kotlin_application.data.Like
import com.example.kotlin_application.data.LikeInput
import com.example.kotlin_application.navigation.Screens
import com.example.kotlin_application.ui.theme.goldYellowHex
import com.example.kotlin_application.viewmodel.CommentViewModel
import com.example.kotlin_application.viewmodel.ForumViewModel
import com.example.kotlin_application.viewmodel.LikeViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.QuerySnapshot

@ExperimentalComposeUiApi
@Composable
fun SingleForumScreen (navController: NavController, forumId: String, viewModel: ForumViewModel = androidx.lifecycle.viewmodel.compose.viewModel(), commentViewModel: CommentViewModel = androidx.lifecycle.viewmodel.compose.viewModel(), likeViewModel: LikeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {

    //Test with single forum screen based on dummy data

    //Set image height based on screen height
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val imageHeight = with(LocalDensity.current) { screenHeight * 0.3f }

    //Set effect to fetch single forum screen
    LaunchedEffect(forumId, viewModel) {
        viewModel.getSingleForum(forumId);
    }

    //Set effect to fetch likes
    LaunchedEffect(forumId, likeViewModel ) {
        likeViewModel.fetchLikes(forumId);
    }


    //Single forum data
    val singleForum = viewModel.singleForum;
    
    //Check user is logged in or not
    val checkUserIsNull = remember(FirebaseAuth.getInstance().currentUser?.email) {
        FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()
    }

    //Get uid from firebase
    val uid = FirebaseAuth.getInstance()?.uid;

    //Get username from firebase
    val username = FirebaseAuth.getInstance()?.currentUser?.email?.split("@")?.get(0);
    
    //Get like from like view model
    val likes = likeViewModel.likes;
    
    //Check like or dislike by user
    val likeOrDislike = likes.any { it?.userId == uid && it?.forumId == forumId }

    //Get context for Toast
    val context = LocalContext.current;

    //Get like by forum id and userid
    
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
            if (!checkUserIsNull) {
                renderFloatingButtonActionToAddComment(navController, forumId)
            }
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

                Row(horizontalArrangement = Arrangement.Center) {
                    if (likes.size == 0) {
                        Text(text = "0 like for this forum", style = TextStyle(color = MaterialTheme.colors.onBackground, fontWeight = FontWeight.Bold, fontSize = 16.sp), modifier = Modifier.padding(12.dp))
                    } else if (likes?.size == 1 && !likeOrDislike) {
                        Text(text = "${likes.size} person likes for this forum", style = TextStyle(color = MaterialTheme.colors.onBackground, fontWeight = FontWeight.Bold, fontSize = 13.sp), modifier = Modifier.padding(12.dp))
                    } else if (likes?.size == 1 && likeOrDislike) {
                        Text(text = "You like for this forum", style = TextStyle(color = MaterialTheme.colors.onBackground, fontWeight = FontWeight.Bold, fontSize = 13.sp), modifier = Modifier.padding(12.dp))
                    } else {
                        Text(text = if (!likeOrDislike) "${likes.size} people like this forum" else "You and other ${likes.size -1} people like this forum", style = TextStyle(color = MaterialTheme.colors.onBackground, fontWeight = FontWeight.Bold, fontSize = 13.sp), modifier = Modifier.padding(12.dp))
                    }

                    Spacer(modifier = Modifier.width(10.dp))
                    if (!likeOrDislike && !checkUserIsNull) {
                    Button(onClick = { likeViewModel.saveLike(LikeInput(forumId = forumId, userId = uid, username = username), context = context) }, colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.onBackground,
                        contentColor = Color.White)) {
                        Row(horizontalArrangement = Arrangement.Center) {
                            Icon(imageVector = Icons.Default.Favorite, contentDescription = "Like")
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(text = "Like", modifier = Modifier.padding(2.dp))
                        }
                    }
                } else if (likeOrDislike && !checkUserIsNull) {
                    val singleLike = likes.filter { it?.forumId == forumId && it?.userId == uid }?.get(0);


                     Button(onClick = { likeViewModel.deleteLike(singleLike?.id as String, context = context) }, colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.onBackground,
                        contentColor = Color.White)) {
                        Row() {
                            Icon(imageVector = Icons.Default.Favorite, contentDescription = "Like", tint = goldYellowHex)
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(text = "Like", modifier = Modifier.padding(2.dp), color = goldYellowHex)
                        }
                    }
                }
                }


                Spacer(modifier = Modifier.height(5.dp))
                Text(text = "Comments: ", style = TextStyle(color = MaterialTheme.colors.onBackground, fontWeight = FontWeight.Bold, fontSize = 18.sp), modifier = Modifier.padding(3.dp))
                Spacer(modifier = Modifier.height(5.dp))
                Button(onClick = { navController.navigate(Screens.CommentScreen.name + "/${forumId}") }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.onBackground,
                    contentColor = Color.White)) {
                    Text(text = "Click to see comments", style = TextStyle(color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp), modifier = Modifier.padding(10.dp), textAlign = TextAlign.Center)
                }


            }
        }
    }






}



@Composable
fun renderFloatingButtonActionToAddComment (navController: NavController, forumId: String) {


    FloatingActionButton(onClick = { navController.navigate(Screens.AddCommentScreen.name + "/${forumId}") }, shape = CircleShape, backgroundColor = MaterialTheme.colors.onBackground) {
        Text(text = "Add New Comment", modifier = Modifier.padding(10.dp),style = TextStyle(color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold), textAlign = TextAlign.Center)
    }
}