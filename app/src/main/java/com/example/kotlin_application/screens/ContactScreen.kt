package com.example.kotlin_application.screens

import androidx.compose.material.Text
import androidx.navigation.NavController
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlin_application.ui.theme.goldYellowHex
import com.example.kotlin_application.viewmodel.ChatVIewModel
import androidx.compose.ui.unit.sp
import com.example.kotlin_application.data.*
import com.example.kotlin_application.viewmodel.UserProfileViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@Composable
fun ContactScreen (navController: NavController, userIds: List<String>?, chatId : String?) {


    //Set view model for chat
    val chatViewModel : ChatVIewModel = viewModel();

    //Set state for message
    val message = remember {
        mutableStateOf("");
    }

    //Set message valid
    val messageIsValid = remember (message.value) {
        message.value.trim().isNotEmpty()
    }

    //Get id of current user
    val uid = FirebaseAuth.getInstance().uid.toString();

    //Set context for Toast
    val context = LocalContext.current;

    //Set effect to get chat room
    LaunchedEffect(chatId) {
        if (chatId != "null") {
            chatViewModel.fetchRoomWithRoomId(chatId as String);
        } else {
            chatViewModel.fetchRoomBasedOnUserIds(userIds as List<String>, context);
            Log.d("user ids::", "$userIds")
        }
    };
    
    //Set state for that single chat room

    val single_chat_room : Chat? = chatViewModel.chatRooms.firstOrNull();

    //Set controller for keyboard
    val keyboardController = LocalSoftwareKeyboardController.current;

    val coroutineScope = rememberCoroutineScope();
    val listState = rememberLazyListState();





    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack()}) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Go Back"
                        )
                    }
                },
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(text = "Chat Room")
                    }
                },
                backgroundColor = MaterialTheme.colors.onBackground,
                contentColor = MaterialTheme.colors.onSecondary
            )
        },
        bottomBar = {
            OutlinedTextField(
                value = message.value,
                onValueChange = {
                    message.value = it
                },
                label = {
                    Text(
                        "Type Your Message", style = TextStyle(color = MaterialTheme.colors.onBackground)
                    )
                },
                maxLines = 1,
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 1.dp)
                    .fillMaxWidth(),
                //.weight(weight = 0.09f,fill = true ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
                keyboardActions = KeyboardActions(onDone = {


                        keyboardController?.hide();


                }),
                placeholder = {
                    Text(text = "Enter your message...!", style = TextStyle(color = goldYellowHex))
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colors.onBackground,
                    unfocusedBorderColor = MaterialTheme.colors.onBackground,
                    disabledBorderColor = MaterialTheme.colors.onBackground
                ),
                singleLine = true,
                trailingIcon = {
                    IconButton(
                        onClick = {

                            if (!messageIsValid) {
                                Toast.makeText(context, "Cannot send empty message", Toast.LENGTH_LONG).show();
                            } else {
                                Log.d("Send", "Send successfully!")
                                val messageInput = MessageInput(message.value, uid, Timestamp.now());
                                chatViewModel.addMessagesToChatRoom(messageInput, single_chat_room?.id as String, context = context);
                                Toast.makeText(context, "Send message", Toast.LENGTH_LONG).show();
                                keyboardController?.hide()
                                message.value = ""
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send Button",
                            tint = MaterialTheme.colors.onBackground
                        )
                    }

                }
            )

        }
    ) {
        it
        
        Column(modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()) {

            single_chat_room?.let { room ->
                if (room.messages?.isEmpty() as Boolean) {
                    Text(
                        text = "No messages",
                        modifier = Modifier.fillMaxWidth(),
                        style = TextStyle(
                            color = MaterialTheme.colors.onBackground,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp
                        )
                    )
                } else {

                    LaunchedEffect(room.messages) {
                        coroutineScope.launch {
                            listState.scrollToItem(room.messages.lastIndex)
                        }
                    }

                    LazyColumn(modifier = Modifier.padding(bottom = 50.dp), state = listState) {
                        items(room.messages) {
                            item -> SingleMessage(messageId = item)
                        }
                    }
                }
            }

        }
       
    }
}