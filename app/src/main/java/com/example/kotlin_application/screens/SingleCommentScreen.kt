package com.example.kotlin_application.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Update
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.kotlin_application.data.Comment
import com.example.kotlin_application.data.LikeForCommentInput
import com.example.kotlin_application.data.LikeInput
import com.example.kotlin_application.navigation.Screens
import com.example.kotlin_application.ui.theme.goldYellowHex
import com.example.kotlin_application.viewmodel.CommentViewModel
import com.example.kotlin_application.viewmodel.LikeForCommentModel
import com.google.firebase.auth.FirebaseAuth

@ExperimentalComposeUiApi
@Composable
fun SingleCommentScreen (navController : NavController,singleItem: Comment?, forumId: String ) {

    //Get Comment viewModel
    val commentViewModel: CommentViewModel = viewModel()
    //Get viewModel for likes in comments
    val likeForCommentModel: LikeForCommentModel = viewModel()
    //Get context for toast
    val context = LocalContext.current;

    //Get uid from firebase
    val uid = FirebaseAuth.getInstance().uid;

    //Check user is null
    val checkUserIsNull = remember(FirebaseAuth.getInstance().currentUser?.email) {
        FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()
    }

    //Get username from firebase
    val username = FirebaseAuth.getInstance()?.currentUser?.email?.split("@")?.get(0);

    //Fetch likes for comment
    LaunchedEffect(forumId, singleItem?.id) {
        likeForCommentModel.fetchLikesForSingleComment(forumId, singleItem?.id as String);
    }


    //Likes for comment from model
    val likes = likeForCommentModel.likesForComment;

    //Like or dislike by user for the comment
    val likeOrDislike = likes.any { it?.userId == uid && it?.forumId == forumId && it?.commentId == singleItem?.id}


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

            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                if (likes.size == 0) {
                    Text(text = "0 like for this comment", style = TextStyle(color = MaterialTheme.colors.onBackground, fontWeight = FontWeight.Bold, fontSize = 16.sp), modifier = Modifier.padding(vertical = 12.dp))
                } else if (likes?.size == 1 && !likeOrDislike) {
                    Text(text = "${likes.size} person likes for this comment", style = TextStyle(color = MaterialTheme.colors.onBackground, fontWeight = FontWeight.Bold, fontSize = 13.sp), modifier = Modifier.padding(vertical = 12.dp))
                } else if (likes?.size == 1 && likeOrDislike) {
                    Text(text = "You like for this comment", style = TextStyle(color = MaterialTheme.colors.onBackground, fontWeight = FontWeight.Bold, fontSize = 13.sp), modifier = Modifier.padding(vertical = 12.dp))
                } else {
                    Text(text = if (!likeOrDislike) "${likes.size} people like this comment" else "You and other ${likes.size -1} people like this comment", style = TextStyle(color = MaterialTheme.colors.onBackground, fontWeight = FontWeight.Bold, fontSize = 13.sp), modifier = Modifier.padding(vertical = 12.dp))
                }

                Spacer(modifier = Modifier.width(10.dp))
                if (!likeOrDislike && !checkUserIsNull) {
                    Button(onClick = { likeForCommentModel.saveLikeToComment(LikeForCommentInput(userId = uid, username = username, forumId = forumId, commentId = singleItem?.id as String), context = context) }, colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.onBackground,
                        contentColor = Color.White)) {
                        Row(horizontalArrangement = Arrangement.Center) {
                            Icon(imageVector = Icons.Default.Favorite, contentDescription = "Like")
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(text = "Like", modifier = Modifier.padding(2.dp))
                        }
                    }
                } else if (likeOrDislike && !checkUserIsNull) {
                    val singleLike = likes.filter { it?.forumId == forumId && it?.userId == uid && it?.commentId == singleItem?.id }?.get(0);


                    Button(onClick = { likeForCommentModel.deleteLikeForComment(singleLike?.id as String, context = context) }, colors = ButtonDefaults.buttonColors(
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
            Spacer(modifier = Modifier.height(20.dp))
            Column() {
                Text(text = "Comment Content: ${singleItem?.comment}", style = TextStyle(color = MaterialTheme.colors.onSecondary, fontWeight = FontWeight.Bold, fontSize = 16.sp), modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = if (singleItem?.userId == uid) "Created by: You" else "Created by: ${singleItem?.username}", style = TextStyle(color = MaterialTheme.colors.onSecondary, fontWeight = FontWeight.Bold, fontSize = 16.sp))
            }
            Spacer(modifier = Modifier.height(20.dp))
            if (singleItem?.userId == uid) Row(modifier = Modifier
                .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(8.dp))
                .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                IconButton(onClick = { commentViewModel.deleteComment(singleItem?.id as String, context) },modifier = Modifier
                    .background(color = MaterialTheme.colors.onBackground)
                    .fillMaxWidth()) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete comment", tint = goldYellowHex)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            if (singleItem?.userId == uid) {
                IconButton(onClick = { navController.navigate(Screens.UpdateCommentScreen.name + "/${singleItem?.id}") },modifier = Modifier
                    .background(color = MaterialTheme.colors.onBackground)
                    .fillMaxWidth()) {
                    Icon(imageVector = Icons.Default.Update, contentDescription = "Update comment", tint = goldYellowHex)
                }
            }


        }
    }
}