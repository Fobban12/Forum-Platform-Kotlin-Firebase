package com.example.kotlin_application.screens

import android.widget.Toast
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlin_application.data.Message
import com.example.kotlin_application.viewmodel.ChatVIewModel
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope

@Composable
fun SingleMessage (messageId : String) {

//    //Get chat view model
    val chatVIewModel : ChatVIewModel = viewModel();

    val singleMessage = remember { mutableStateOf<Message?>(null) }

    //Get local context for Toast
    val context = LocalContext.current;
//
    LaunchedEffect(messageId) {
        chatVIewModel.fetchSingleMessage(messageId, context = context)?.let { message ->
            // Set the fetched message in the singleMessage state variable
            singleMessage.value = message
        };
    }

    Text(text = "${singleMessage.value?.content}");
}