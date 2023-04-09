package com.example.kotlin_application.screens

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlin_application.viewmodel.ChatVIewModel

@Composable
fun SingleMessage (messageId : String) {

    //Get chat view model
    val chatVIewModel : ChatVIewModel = viewModel();

    //Get local context for Toast
    val context = LocalContext.current;

    LaunchedEffect(messageId) {
        chatVIewModel.fetchSingleMessage(messageId = messageId, context = context);
    }

    //Get single message
    val single_message = chatVIewModel.singleMessage;

    Text(text = "${single_message.value?.content}")
}