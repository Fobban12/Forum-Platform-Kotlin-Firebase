package com.example.kotlin_application.data

import java.util.Date

data class User(
    val id:Int,
    val Username: String?,
    val Email: String,
    val Password: Int,
    val Date_Created: Date,
    val Flair:String,
)