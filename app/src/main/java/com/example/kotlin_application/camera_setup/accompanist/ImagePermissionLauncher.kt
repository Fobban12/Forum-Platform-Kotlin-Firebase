package com.example.kotlin_application.camera_setup.accompanist

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.kotlin_application.permissions.CAMERA_PERMISSION
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun cameraPermissionState(): PermissionState {
    return rememberPermissionState(permission = CAMERA_PERMISSION)
}

/**
 * Launches the permission only to take a picture.
 * This launcher cannot be reused for other permission launcher type, since the return
 * result will be of type Bitmap.
 */
@Composable
fun imagePermissionLauncher(): ManagedActivityResultLauncher<Void?, Bitmap?> {
    val result = remember { mutableStateOf(placeHolderBitmap) }

    return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            result.value = bitmap
        }
    }

    // Use the result
}

val placeHolderBitmap: Bitmap = BitmapFactory.decodeResource(
    Resources.getSystem(),
    android.R.drawable.ic_menu_camera
)