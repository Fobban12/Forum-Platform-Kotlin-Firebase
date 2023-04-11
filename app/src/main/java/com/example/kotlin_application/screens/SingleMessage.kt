package com.example.kotlin_application.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
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
import com.example.kotlin_application.ui.theme.goldYellowHex
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun SingleMessage (messageId : String) {

   //Get chat view model
    val chatVIewModel : ChatVIewModel = viewModel();

    //Set state for single message
    val singleMessage = remember { mutableStateOf<Message?>(null) }

    //Set width based on screen width
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val chatBoxWidth = with(LocalDensity.current) { screenWidth * 0.8f }

    //Get uid from firebase
    val uid = FirebaseAuth.getInstance().uid.toString();

    //Get local context for Toast
    val context = LocalContext.current;

    //Set effect to fetch single message
    LaunchedEffect(messageId) {
        chatVIewModel.fetchSingleMessage(messageId, context = context)?.let { message ->
            // Set the fetched message in the singleMessage state variable
            singleMessage.value = message
        };
    }

//    //Set string for timestamp
    val date = singleMessage.value?.createdAt?.toDate()

    // format date time value
    val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    val dateTime = date?.let { dateFormat.format(it) } ?: "N/A"

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 10.dp, horizontal = 10.dp), horizontalArrangement = if (uid == singleMessage.value?.senderId) Arrangement.End else Arrangement.Start) {
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
                    Text(text = if (uid == singleMessage.value?.senderId) "Your message created at ${dateTime}" else "${singleMessage.value ?.senderId}'s message created at ${dateTime}", modifier = Modifier.padding(10.dp), textAlign = TextAlign.Start)
                }
            }
        }

    }
}