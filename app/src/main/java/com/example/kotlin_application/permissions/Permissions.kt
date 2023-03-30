package com.example.kotlin_application.permissions

import android.Manifest


const val CAMERA_PERMISSION = Manifest.permission.CAMERA
const val BATTERY_STATS = Manifest.permission.BATTERY_STATS
const val SET_TIME_ZONE = Manifest.permission.SET_TIME_ZONE

fun samplePermissions(): List<String> = listOf(
    BATTERY_STATS,
    SET_TIME_ZONE
)