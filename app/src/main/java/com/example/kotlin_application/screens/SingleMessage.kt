package com.example.kotlin_application.screens

import android.widget.Space
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlin_application.data.Message
import com.example.kotlin_application.viewmodel.ChatVIewModel
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.kotlin_application.data.UserProfile
import com.example.kotlin_application.ui.theme.goldYellowHex
import com.example.kotlin_application.viewmodel.UserProfileViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.maps.android.compose.Circle
import androidx.compose.ui.res.*;
import androidx.compose.ui.unit.sp;


@OptIn(ExperimentalCoilApi::class)
@Composable
fun SingleMessage (messageId : String) {

   //Get chat view model
    val chatVIewModel : ChatVIewModel = viewModel();
    
    //Get user profile view model
    val userProfileViewModel : UserProfileViewModel = viewModel();

    //Set state for single message
    val singleMessage = remember { mutableStateOf<Message?>(null) }

    //Set state for single profile
    val singleUserProfile = remember {
        mutableStateOf<UserProfile?>(null)
    };

    //Set width based on screen width
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val chatBoxWidth = with(LocalDensity.current) { screenWidth * 0.8f }

    //Set width for avatar based on screen width
    val avatarWidth = if (singleUserProfile.value?.image != null || singleUserProfile.value?.image.toString().isNotEmpty()) with(LocalDensity.current) { screenWidth * 0.2f} else with(
        LocalDensity.current) {screenWidth * 0f};
    val avatarHeight = if (singleUserProfile.value?.image != null || singleUserProfile.value?.image.toString().isNotEmpty()) with(LocalDensity.current) { screenWidth * 0.2f} else with(
        LocalDensity.current) {screenWidth * 0f};

    //Get uid from firebase
    val uid = FirebaseAuth.getInstance().uid.toString();

    //Get local context for Toast
    val context = LocalContext.current;

    //Set effect to fetch single message
    LaunchedEffect(messageId) {
        chatVIewModel.fetchSingleMessage(messageId, context = context)?.let { message ->
            // Set the fetched message in the singleMessage state variable
            singleMessage.value = message
        }
    };

    //Set effect to fetch user profile of that person created the message
    LaunchedEffect(singleMessage.value) {

        if (singleMessage.value != null) {
            userProfileViewModel.fetchSingleUserProfileWithPromiseYield(singleMessage?.value?.senderId as String).let { it ->
                singleUserProfile.value = it;
            }
        }
    }

   //Set string for timestamp
    val date = singleMessage.value?.createdAt?.toDate()

    // format date time value
    val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    val dateTime = date?.let { dateFormat.format(it) } ?: "N/A"

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 10.dp, horizontal = 10.dp), horizontalArrangement = if (uid == singleMessage.value?.senderId) Arrangement.End else Arrangement.Start) {

        Spacer(modifier = Modifier.width(8.dp))
        Card (
            modifier = Modifier
                .width(chatBoxWidth),// set background color of the card
            shape = RoundedCornerShape(16.dp), // set border radius of the card
            elevation = 8.dp, // set elevation of the card
            border = BorderStroke(2.dp, Color.Black),
            backgroundColor = goldYellowHex
        ) {
            Box(modifier = Modifier
                .fillMaxSize()) {

                    Column() {
                        Text(text =  "${singleMessage.value?.content}", modifier = Modifier.padding(10.dp),style = TextStyle(color = Color.Black,fontSize = 16.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = if (uid == singleMessage.value?.senderId) "Your message created at ${dateTime}" else "${singleUserProfile.value?.username}'s message created at ${dateTime}", modifier = Modifier.padding(10.dp), textAlign = TextAlign.Start)
                        Spacer(modifier = Modifier.height(8.dp))
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
                                         rememberImagePainter(
                                            data = singleUserProfile.value?.image,
                                            builder = {
                                                crossfade(true)
                                             
                                            }
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
                            Text(text = "No Avatar", textAlign = TextAlign.Center, modifier = Modifier.padding(5.dp).fillMaxWidth(), style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 8.sp));
                        }
                        
                    }
            
                
                
                
               
            }
        }

    }
}