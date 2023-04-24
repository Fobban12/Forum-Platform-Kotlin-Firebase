package com.example.kotlin_application.screens

import android.widget.Space
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.kotlin_application.data.Chat
import com.example.kotlin_application.data.Message
import com.example.kotlin_application.data.UserProfile
import com.example.kotlin_application.navigation.Screens
import com.example.kotlin_application.ui.theme.goldYellowHex
import com.example.kotlin_application.viewmodel.ChatVIewModel
import com.example.kotlin_application.viewmodel.UserProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

@Composable
fun SingleMessageBox (navController: NavController, item: Chat?) {

    //User profile view model
    val userProfileViewModel : UserProfileViewModel = viewModel();

    //Chat view model
    val chatViewModel : ChatVIewModel = viewModel();

    //Set state for single profile
    val singleUserProfile = remember {
        mutableStateOf<UserProfile?>(null)
    };

    //Set state for single message
    val singleMessage = remember {
        mutableStateOf<Message?>(null);
    }

    //Get uid from chat list screen
    val uid = FirebaseAuth.getInstance().uid.toString();

    //Filter info of another user info in the chat room
    val anotherUserId = item?.userIds?.filter { it -> it != uid }?.firstOrNull();


 //    //Filter the last message in that chat room
    val lastMessageId = item?.messages?.lastOrNull();

    //Set effect to fetch user profile of that person created the message
    LaunchedEffect(anotherUserId) {

        userProfileViewModel.fetchSingleUserProfileWithPromiseYield(anotherUserId as String).let { it ->
                singleUserProfile.value = it;
        }
    };

    
//
    //Set context for Toast
    val context = LocalContext.current;

    //Set effect to fetch the last message for that chat room
    LaunchedEffect(lastMessageId) {
        if (lastMessageId != null) {
            chatViewModel.fetchSingleMessage(lastMessageId, context = context).let {
                singleMessage.value = it;
            };
        }

    };

    //Set width based on screen width
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val chatBoxWidth = with(LocalDensity.current) { screenWidth * 0.8f }

//    Set width for avatar based on screen width
    val avatarWidth = if (singleUserProfile.value?.image != null || singleUserProfile.value?.image.toString().isNotEmpty()) with(
        LocalDensity.current) { screenWidth * 0.1f} else with(
        LocalDensity.current) {screenWidth * 0f};
    val avatarHeight = if (singleUserProfile.value?.image != null || singleUserProfile.value?.image.toString().isNotEmpty()) with(
        LocalDensity.current) { screenWidth * 0.1f} else with(
        LocalDensity.current) {screenWidth * 0f};

//    Set condition for the last message
    val contentMessage = if (singleMessage.value == null) "No message created yet!" else if ((singleMessage.value?.content?.length ?: 0) <= 5) singleMessage.value?.content else "${singleMessage.value?.content?.substring(0, 6)}...";


    Card (
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate(Screens.ContactScreen.name + "/${item?.id}/null"); },// set background color of the card
        shape = RoundedCornerShape(10.dp), // set border radius of the card
        elevation = 8.dp, // set elevation of the card
        border = BorderStroke(2.dp, Color.Black),
        backgroundColor = goldYellowHex,
    ) {
        //Set string for timestamp
        val date = item?.createdAt?.toDate()

        // format date time value
        val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        val dateTime = date?.let { dateFormat.format(it) } ?: "N/A";

        Row() {
            Column(modifier = Modifier.padding(10.dp)) {
                Text(text = "Chat with: ${singleUserProfile.value?.username}", style = TextStyle(fontWeight = FontWeight.Bold))
                Text(text = "Chat room created at: $dateTime", style = TextStyle(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(5.dp))
                Row() {
                    Text(text = if (singleMessage.value == null) "" else if (uid == singleMessage.value?.senderId) "You sent: " else "${singleUserProfile.value?.username} sent: ")
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(text = "$contentMessage")
                }
            }

            Column(modifier = Modifier.padding(5.dp), verticalArrangement = Arrangement.Center) {
                Column(modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {


                    Column(
                        modifier = Modifier
                            .width(avatarWidth)
                            .height(avatarHeight)
                            .clip(CircleShape)
                    ) {
//
                        val painter: Painter =
                            rememberAsyncImagePainter(
                                ImageRequest.Builder(LocalContext.current)
                                    .data(data = singleUserProfile.value?.image).apply(block = fun ImageRequest.Builder.() {
                                        crossfade(true)
                                    }).build()
                            );
                        Image(
                            painter = painter,
                            contentDescription = "User profile avatar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .border(BorderStroke(width = 1.dp, color = Color.Black))
                        )
//
                    }
                }

                if (singleUserProfile.value?.image == null || singleUserProfile.value?.image.toString().isEmpty()) {
                    Text(text = "No Avatar", textAlign = TextAlign.Center, modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth(), style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 8.sp));
                }
            }
        }


    }
    Spacer(modifier = Modifier.height(20.dp))
}