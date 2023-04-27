package com.example.kotlin_application.screens

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.ArrowForward
import androidx.compose.material.icons.sharp.CheckCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.kotlin_application.navigation.Screens
import com.example.kotlin_application.viewmodel.UserProfileViewModel
import com.google.accompanist.permissions.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(navController: NavController, userProfileId : String) {
    val permissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)

    when(permissionState.status){
        is PermissionStatus.Granted -> {
            CameraMainView(navController, userProfileId);
        }
        else -> {
            if (permissionState.status.shouldShowRationale) {
                //Denied once and should show the dialog again
                Text(text = "You should really give permission to use this feature")

            } else {
                //When first time starting the application and if permission denied twice (or in setting)
                Text(text = "You've denied permission for camera")
            }
            SideEffect {
                permissionState.launchPermissionRequest()
            }
        }
    }
}

@Composable
fun CameraMainView(navController: NavController, userProfileId: String) {

    var lensF = CameraSelector.LENS_FACING_FRONT
    var lensB = CameraSelector.LENS_FACING_BACK


    var imageUri: Uri? by remember{ mutableStateOf(null) }
    var lensFacing: Int by remember { mutableStateOf(lensB) }

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val previewView = remember { PreviewView(context) }
    val imageCapture: ImageCapture = remember { ImageCapture.Builder().build() }

    val camSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

    //Get storage reference
    val ref: StorageReference = FirebaseStorage.getInstance().reference;
    
    //Get user profile view model
    val userProfileViewModel: UserProfileViewModel = viewModel();

    //Get user UID
    val uid = FirebaseAuth.getInstance().uid.toString();


    //Check user is logged in or not
    val checkUserIsNull = remember(FirebaseAuth.getInstance().currentUser?.email) {
        FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()
    };

    //Set effect to fetch single user id

    LaunchedEffect(uid, checkUserIsNull, userProfileViewModel) {
        userProfileViewModel.fetchSingleUserProfile(uid as String);
    }

    //Get state from user profile from view model
    val singleUser = userProfileViewModel.singleUserProfile;


    if(imageUri == null){
        SideEffect {
            ProcessCameraProvider.getInstance(context).apply {
                addListener({
                    val camProvider = get()
                    val preview = Preview.Builder().build()
                    preview.setSurfaceProvider(previewView.surfaceProvider)
                    camProvider.unbindAll()
                    camProvider.bindToLifecycle(lifecycleOwner, camSelector, preview, imageCapture  )
                }, ContextCompat.getMainExecutor(context))
            }
        }
    }


    Box(modifier = Modifier.fillMaxSize()){
        if(imageUri == null){
            CameraPreview(previewView, {
                lensFacing = if(lensFacing == lensB ) lensF else lensB
            }) {
                takePhoto(context, imageCapture) { imageUri = it }
            }
        } else {
            imageUri?.let {
                ref.child("/users/${singleUser.value?.userId}/profile/profilePic/image")
                    .putFile(it).addOnSuccessListener {
                        val urlDownload =
                            ref.child("/users/${singleUser.value?.userId}/profile/profilePic/image").downloadUrl
                        urlDownload.addOnSuccessListener {

                            userProfileViewModel.updateImage(
                                userProfileId,
                                it.toString(),
                                context = context
                            )
                            navController.navigate(
                                Screens.ProfileScreen.name
                            );
                            Log.d("Success", "Success!")
                        }
                            .addOnFailureListener {
                                Toast.makeText(
                                    context,
                                    "Fail to post image profile",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }


                    }

            }
        }
    }
}

@Composable
fun CameraPreview(
    previewView: PreviewView,
    changeLens: () -> Unit,
    takePhoto: () -> Unit
) {
    Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize()){
        AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())
        Column() {
            Icon(
                Icons.Sharp.CheckCircle,
                "takePhoto",
                modifier = Modifier
                    .size(80.dp)
                    .clickable { takePhoto() },
                tint = Color.White
            )
            Icon(
                Icons.Sharp.ArrowForward,
                "changeLens",
                modifier = Modifier
                    .size(80.dp)
                    .clickable { changeLens() },
                tint = Color.White
            )
        }
    }
}



fun takePhoto(
    context: Context,
    imageCapture: ImageCapture,
    onImageCaptured: (Uri?) -> Unit
){
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P){
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
        }
    }

    val outputOptions = ImageCapture.OutputFileOptions.Builder(
        context.contentResolver,
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        contentValues
    ).build()

    imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(context), object: ImageCapture.OnImageSavedCallback{
        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
            Log.d("------", outputFileResults.savedUri.toString() )
            onImageCaptured(outputFileResults.savedUri)
        }

        override fun onError(exception: ImageCaptureException) {
            Log.e("", "Take photo error", exception)
        }
    })
}