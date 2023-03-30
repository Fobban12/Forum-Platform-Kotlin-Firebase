package com.example.kotlin_application.permissions

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

/**
 * Checks if the passed permission is granted.
 * You can check only one permission.
 */
fun isPermissionGranted(
    context: Context,
    permission: String
): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        permission
    ) == PackageManager.PERMISSION_GRANTED
}

/**
 * Checks if all permission are granted, permissions can be parsed as list.
 * This function can be invoked by calling [toTypedArray] when passed to launcher.
 */
fun isPermissionGranted(
    context: Context,
    permission: List<String>
): Boolean {
    return permission.all { eachPermission ->
        ContextCompat.checkSelfPermission(
            context,
            eachPermission
        ) == PackageManager.PERMISSION_GRANTED
    }
}