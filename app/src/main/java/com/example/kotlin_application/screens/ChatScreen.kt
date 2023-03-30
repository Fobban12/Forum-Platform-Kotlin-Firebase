package com.example.kotlin_application.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.kotlin_application.navigation.Screens
import com.example.kotlin_application.viewmodel.ChatVIewModel
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.kotlin_application.data.Constants

@ExperimentalComposeUiApi
@Composable
fun ChatScreen(
    navController: NavController,
    chatVIewModel: ChatVIewModel = viewModel(),
) {
    //Scaffold
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    //Check user is logged in or not
    val checkUserIsNull = remember(FirebaseAuth.getInstance().currentUser?.email) {
        FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()
    }

    //Get username from firebase
    val username = FirebaseAuth.getInstance().currentUser?.email?.split("@")?.get(0);

    val message: String by chatVIewModel.message.observeAsState(initial = "")
    val messages: List<Map<String, Any>> by chatVIewModel.messages.observeAsState(
        initial = emptyList<Map<String, Any>>().toMutableList()
    )

    Scaffold(
        topBar = {
            ChatScreenTopBar(
                navController,
                IconClick = {
                    navController.navigate(Screens.MainScreen.name)
                })
        },
        bottomBar = {
            OutlinedTextField(
                value = message,
                onValueChange = {
                    chatVIewModel.updateMessage(it)
                },
                label = {
                    Text(
                        "Type Your Message"
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
                singleLine = true,
                trailingIcon = {
                    IconButton(
                        onClick = {
                            chatVIewModel.addMessage()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send Button"
                        )
                    }

                }
            )

        }
    ){it
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
                //error : cannot import ColumnInstance.
               // .weight(weight = 0.85f,fill = true ),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            reverseLayout = true
        ) {
            items(messages) { message ->
                val isCurrentUser = message[Constants.IS_CURRENT_USER] as Boolean

                SingleMessage(
                    message = message[Constants.MESSAGE].toString(),
                    isCurrentUser = isCurrentUser
                )
            }
        }


    }

}
    
    

@ExperimentalComposeUiApi
@Composable
fun ChatScreenTopBar(navController: NavController, IconClick: () -> Unit)
{
    val scaffoldState = rememberScaffoldState();

    TopAppBar(
        navigationIcon = {
            IconButton(onClick = IconClick) {
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
                Text(text = "Chat Screen")
            }
        },
        backgroundColor = MaterialTheme.colors.onBackground,
        contentColor = MaterialTheme.colors.onSecondary
    )

}

@ExperimentalComposeUiApi
@Composable
fun SingleMessage (message: String, isCurrentUser: Boolean) {
    Card (
        shape = RoundedCornerShape(16.dp),
        backgroundColor = if(isCurrentUser) MaterialTheme.colors.primary else Color.White
            ){
        Text(
            text = message,
            textAlign =
                if (isCurrentUser)
                    TextAlign.End
        else
            TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            color = if(!isCurrentUser) MaterialTheme.colors.primary else Color.White
        )

    }
    
}