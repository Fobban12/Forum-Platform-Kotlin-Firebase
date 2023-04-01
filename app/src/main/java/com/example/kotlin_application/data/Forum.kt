package com.example.kotlin_application.data

import com.google.firebase.firestore.ServerTimestamp
import com.google.firebase.Timestamp

data class Forum(
    val id: String? = null,
    val title: String? = null,
    val type: String? = null,
    val description: String? = null,
    val image: String? = null,
    @ServerTimestamp
    val createdAt: Timestamp?,
    val userId: String?,
    val username: String?
    )
