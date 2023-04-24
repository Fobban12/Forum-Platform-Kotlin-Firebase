package com.example.kotlin_application.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.kotlin_application.data.Forum
import com.example.kotlin_application.navigation.Screens
import com.example.kotlin_application.ui.theme.goldYellowHex
import com.example.kotlin_application.viewmodel.ForumViewModel
import com.example.kotlin_application.viewmodel.UserProfileViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SingleForum (item : Forum, navController : NavController, uid : String?) {

    //Get Forum viewModel
    val viewModel: ForumViewModel = viewModel()
    //Get user profile view model
    val userProfileViewModel: UserProfileViewModel = viewModel();

    val mutableItem = remember { mutableStateOf(item) }
    //Set state for alert dialog
    val showDialog = remember { mutableStateOf(false) };
    //Set local context for Toast
    val context = LocalContext.current;

    //Get user UID
    val uid = FirebaseAuth.getInstance().uid.toString();


    //Check user is logged in or not
    val checkUserIsNull = remember(FirebaseAuth.getInstance().currentUser?.email) {
        FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()
    };

    //Set effect to fetch single user id

    //Set effect to fetch single user id
    if(!checkUserIsNull){ LaunchedEffect(uid, checkUserIsNull, userProfileViewModel) {
        userProfileViewModel.fetchSingleUserProfile(uid as String);
    }}

    //Get state from user profile from view model
    val singleUser = userProfileViewModel.singleUserProfile;


    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            backgroundColor = Color.LightGray,
            title = {
                Text(text = "Delete forum", style = TextStyle(textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, fontSize = 16.sp))
            },
            text = {
                Text(text = "Are you sure you want to delete your forum with title ${mutableItem.value.title} you created?", style = TextStyle(color = MaterialTheme.colors.onBackground,textAlign = TextAlign.Left, fontWeight = FontWeight.Bold, fontSize = 14.sp))
            },
            confirmButton = {
                Button(onClick = { showDialog.value = false; viewModel.deleteForum(item.id as String, context = context,  singleUser.value?.userId as String, item.idImage as String ); navController.navigate(Screens.MainScreen.name)}, colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.onBackground,
                    contentColor = Color.White)) {
                    Text(text = "Yes, sure")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog.value = false }, colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.onBackground,
                    contentColor = Color.White)) {
                    Text(text = "No")
                }
            }
        )

    }

    Spacer(modifier = Modifier.height(10.dp))
    Box(
        modifier = Modifier
            .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(8.dp))
            .background(MaterialTheme.colors.onSurface)
            .padding(vertical = 5.dp, horizontal = 10.dp)
            .height(IntrinsicSize.Min),

    ) {
        Column(verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {

            if (uid == item.userId) {
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {

                    IconButton(
                        onClick = {
                           showDialog.value = !showDialog.value
                        },
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colors.onBackground,
                                CircleShape
                            )
                            .size(20.dp),
                    ) {
                        Icon(imageVector = Icons.Default.Remove, contentDescription = "Remove forum", tint = goldYellowHex)
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier
                .padding(2.dp)
                .fillMaxHeight(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
                 ) {
                val painterState = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current).data(data = "${mutableItem.value.image}")
                        .apply(block = fun ImageRequest.Builder.() {

                        }).build()
                )

                Image(
                    painter = painterState,
                    contentDescription = "Image for forum",
                    modifier = Modifier.height(100.dp).width(100.dp)
                )
                Column(
                    modifier = Modifier.padding(20.dp),


                ) {
                    Text(text = "Title: ${mutableItem.value.title}", style = TextStyle(color = MaterialTheme.colors.onSecondary, fontWeight = FontWeight.Bold, fontSize = 18.sp, textAlign = TextAlign.Left))
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(text = "Description : ${mutableItem.value.description}", style = TextStyle(color = MaterialTheme.colors.onSecondary, fontWeight = FontWeight.Bold, fontSize = 12.sp, textAlign = TextAlign.Left
                    ))
                    Button(onClick = { navController.navigate(Screens.SingleForumScreen.name + "/${mutableItem.value.id}") }, modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(), colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.onBackground,
                        contentColor = Color.White
                    )) {
                        Text(text = "Click to see more details", style = TextStyle(textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, fontSize = 10.sp), modifier = Modifier.fillMaxWidth())
                    }


                }

            }
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}


