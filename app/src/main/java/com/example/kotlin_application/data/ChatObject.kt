package com.example.kotlin_application.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

data class ChatObject (
    val id: String?,
    val userIds: List<String>?,
    val messages: List<Message>?,
    @ServerTimestamp
    val createdAt: Timestamp?
        )