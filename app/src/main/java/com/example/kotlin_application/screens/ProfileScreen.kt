package com.example.kotlin_application.screens

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.kotlin_application.camera_setup.accompanist.ProfileScreenWithAccompanist
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.example.kotlin_application.ui.theme.CameraXComposeTheme

@ExperimentalPermissionsApi
@Composable
fun ProfileScreen (navController: NavController) {

        ProfileScreenWithAccompanist();
//    val permissions = if (Build.VERSION.SDK_INT <= 28){
//        listOf(
//            Manifest.permission.CAMERA,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE
//        )
//    }else listOf(Manifest.permission.CAMERA)
//
//    val permissionState = rememberMultiplePermissionsState(
//        permissions = permissions)
//
//    if (!permissionState.allPermissionsGranted){
//        SideEffect {
//            permissionState.launchMultiplePermissionRequest()
//        }
//    }
//
//
//    val context = LocalContext.current
//    val lifecycleOwner = LocalLifecycleOwner.current
//    val configuration = LocalConfiguration.current
//    val screeHeight = configuration.screenHeightDp.dp
//    val screenWidth = configuration.screenWidthDp.dp
//    var previewView:PreviewView
//
//
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
////         we will show camera preview once permission is granted
//        if (permissionState.allPermissionsGranted){
//            Box(modifier = Modifier
//                .height(screeHeight * 0.85f)
//                .width(screenWidth)) {
//                AndroidView(
//                    factory = {
//                        previewView = PreviewView(it)
//                        cameraViewModel.showCameraPreview(previewView, lifecycleOwner)
//                        previewView
//                    },
//                    modifier = Modifier
//                        .height(screeHeight * 0.85f)
//                        .width(screenWidth)
//                )
//            }
//        }
//
//        Box(
//            modifier = Modifier
//                .height(screeHeight*0.15f),
//            contentAlignment = Alignment.Center
//        ){
//            IconButton(onClick = {
//                if (permissionState.allPermissionsGranted){
//                    cameraViewModel.captureAndSave(context)
//                }
//                else{
//                    Toast.makeText(
//                        context,
//                        "Please accept permission in app settings",
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
//            }) {
//
//                Icon(imageVector = Icons.Default.Camera, contentDescription = "Camera")
//
//            }
//        }
//
//    }

}

