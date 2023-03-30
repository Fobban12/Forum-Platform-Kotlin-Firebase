package com.example.kotlin_application.camera_setup.permissionLauncher

import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

/**
 * This launcher can be used with any type of permissions.
 * We can check only one permission is or not granted.
 */
@Composable
fun permissionLauncher(): ManagedActivityResultLauncher<String, Boolean> {

    return rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {  }
}