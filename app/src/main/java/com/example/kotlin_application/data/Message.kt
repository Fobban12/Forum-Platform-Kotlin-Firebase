package com.example.kotlin_application.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

data class Message(
    val id : String,
    val content: String,
    val senderId: String,
    @ServerTimestamp
    val createdAt: Timestamp
)
