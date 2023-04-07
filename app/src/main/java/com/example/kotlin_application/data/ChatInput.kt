package com.example.kotlin_application.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

data class ChatInput(
    val userIds: List<String>?,
    val messages: List<String>?,
    @ServerTimestamp
    val createdAt: Timestamp?
)
