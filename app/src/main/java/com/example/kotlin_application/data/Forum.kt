package com.example.kotlin_application.data

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Forum(
    val id: String,
    val title: String,
    val type: String,
    val description: String?,
    val image: String?,
    @ServerTimestamp
    val createdAt : Date?,
    val userId: String?
    )
