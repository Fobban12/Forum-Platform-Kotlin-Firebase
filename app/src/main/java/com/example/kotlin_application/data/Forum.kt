package com.example.kotlin_application.data

import java.util.Date

data class Forum(
    val id: Int,
    val Title: String,
    val Type: String,
    val Description: String?,
    val Date_Created:Date,
    val User: User
    )
