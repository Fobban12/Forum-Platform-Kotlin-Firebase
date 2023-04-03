package com.example.kotlin_application.data

import com.google.firebase.firestore.ServerTimestamp
import com.google.firebase.Timestamp

data class Forum(
    val id: String? = null,
    val title: String?,
    val type: String?,
    val description: String?,
    val image: String?,
    @ServerTimestamp
    val createdAt: Timestamp?,
    val userId: String?,
    val username: String?
    )
