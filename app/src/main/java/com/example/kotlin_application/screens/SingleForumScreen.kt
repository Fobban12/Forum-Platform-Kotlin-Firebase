package com.example.kotlin_application.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.kotlin_application.data.LikeInput
import com.example.kotlin_application.navigation.Screens
import com.example.kotlin_application.ui.theme.goldYellowHex
import com.example.kotlin_application.viewmodel.ChatVIewModel
import com.example.kotlin_application.viewmodel.ForumViewModel
import com.example.kotlin_application.viewmodel.LikeViewModel
import com.example.kotlin_application.viewmodel.UserProfileViewModel
import com.google.firebase.auth.FirebaseAuth


@ExperimentalComposeUiApi
@Composable
fun SingleForumScreen (navController: NavController, forumId: String) {
    //Get Forum viewModel
    val viewModel: ForumViewModel = viewModel()
    //Get Like viewModel
    val likeViewModel: LikeViewModel = viewModel()
    //Get chat viewModel
    val chatViewModel: ChatVIewModel = viewModel()
    //Get user profile view model
    val userProfileViewModel: UserProfileViewModel = viewModel();

    //Single forum data
    val singleForum = viewModel.singleForum;

    //Get state from user profile from view model
    val singleUser = userProfileViewModel.singleUserProfile;

    //Get like from like view model
    val likes = likeViewModel.likes;

    //Set effect to fetch single forum screen
    LaunchedEffect(forumId, viewModel) {
        viewModel.getSingleForum(forumId);
    }
    //Set effect to fetch likes
    LaunchedEffect(forumId, likeViewModel ) {
        likeViewModel.fetchLikes(forumId);
    }

    //Set image height based on screen height
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val imageHeight = with(LocalDensity.current) { screenHeight * 0.3f }

    //Set image height based on screen height
    val buttonWidth = with(LocalDensity.current) { screenHeight * 0.3f }


    //Check user is logged in or not
    val checkUserIsNull = remember(FirebaseAuth.getInstance().currentUser?.email) {
        FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()
    }
    //Get uid from firebase
    val uid = FirebaseAuth.getInstance()?.uid;

    //Set effect to fetch single user id
    if(!checkUserIsNull){ LaunchedEffect(uid, checkUserIsNull, userProfileViewModel) {
        userProfileViewModel.fetchSingleUserProfile(uid as String);
    }}

    //Check like or dislike by user
    val likeOrDislike = likes.any { it?.userId == uid && it?.forumId == forumId }

    //Get context for Toast
    val context = LocalContext.current;

    //Expanded for top bar drop down
    var expanded = remember {
        mutableStateOf(false)
    };
    
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
                    Text(text = "${singleForum.value?.title}")

                },

                actions = {
                          IconButton(onClick = { expanded.value = !expanded.value }) {
                              Icon(imageVector = Icons.Filled.MoreVert, contentDescription = null) }

                          DropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = false })
                          {
                              if(singleForum.value?.userId == singleUser.value?.userId){ DropdownMenuItem(onClick = { expanded.value =
                                  false; navController.navigate(
                                  Screens.UpdateForum.name + "/$forumId"
                              ) }) {
                                  Text("Edit forum")
                              }}



                          }



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
                val painterState = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = "${singleForum.value?.image}").apply(block = fun ImageRequest.Builder.() {

                        }).build()
                )
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
                                Text(text = "Title: ${singleForum.value?.title}", style = TextStyle(color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp), modifier = Modifier.padding(10.dp))
                                Text(text = "Description: ${singleForum.value?.description}", style = TextStyle(color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp), modifier = Modifier.padding(10.dp))
                                Text(text = "Created by: ${singleForum.value?.username}", style = TextStyle(color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp), modifier = Modifier.padding(10.dp))

                            }

                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(5.dp))

                Row(horizontalArrangement = Arrangement.Center) {
                    if (likes.size == 0) {
                        Text(text = "0 likes", style = TextStyle(color = MaterialTheme.colors.onBackground, fontWeight = FontWeight.Bold, fontSize = 16.sp), modifier = Modifier.padding(12.dp))
                    } else if (likes?.size == 1 && !likeOrDislike) {
                        Text(text = "${likes.size} likes", style = TextStyle(color = MaterialTheme.colors.onBackground, fontWeight = FontWeight.Bold, fontSize = 13.sp), modifier = Modifier.padding(12.dp))
                    } else if (likes?.size == 1 && likeOrDislike) {
                        Text(text = "Liked", style = TextStyle(color = MaterialTheme.colors.onBackground, fontWeight = FontWeight.Bold, fontSize = 13.sp), modifier = Modifier.padding(12.dp))
                    } else {
                        Text(text = if (!likeOrDislike) "${likes.size} liked" else "You and other ${likes.size -1} like this", style = TextStyle(color = MaterialTheme.colors.onBackground, fontWeight = FontWeight.Bold, fontSize = 13.sp), modifier = Modifier.padding(12.dp))
                    }

                    Spacer(modifier = Modifier.width(10.dp))
                    if (!likeOrDislike && !checkUserIsNull) {
                    Button(onClick = { likeViewModel.saveLike(LikeInput(forumId = forumId, userId = uid, username = singleUser.value?.username), context = context) }, colors = ButtonDefaults.buttonColors(
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
                Button(onClick = { navController.navigate(Screens.CommentScreen.name + "/$forumId") }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.onBackground,
                    contentColor = Color.White)) {
                    Text(text = "Click to see comments", style = TextStyle(color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp), modifier = Modifier.padding(10.dp), textAlign = TextAlign.Center)
                }
                Spacer(modifier = Modifier.height(5.dp))

                if (uid != singleForum.value?.userId && !checkUserIsNull) {
                    Row(horizontalArrangement = Arrangement.Start) {
                        Button(onClick = {
                                         val userIds = mutableListOf<String>();
                            userIds.add(uid.toString());
                            userIds.add(singleForum.value?.userId as String);
                            chatViewModel.createOrUpdateRoom(userIds, context = context);

                            val navArgs = listOf(userIds.joinToString(","))
                            Log.d("navArgs", "$navArgs")
                            navController.navigate(Screens.ContactScreen.name + "/null/${navArgs}");
                        }, modifier = Modifier.width(buttonWidth), colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.onBackground,
                            contentColor = Color.White)) {
                            Text(text = "Click to contact", style = TextStyle(color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp), modifier = Modifier.padding(10.dp), textAlign = TextAlign.Center)
                        }
                    }
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