package com.example.kotlin_application.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Comment(
    val id: String?,
    val comment: String?,
    @ServerTimestamp
    val createdAt: Timestamp?,
    val forumId: String?,
    val userId: String?,
    val username: String?,
)


