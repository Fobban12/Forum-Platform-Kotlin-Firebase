package com.example.kotlin_application.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.example.kotlin_application.ui.theme.CameraXComposeTheme
import com.example.kotlin_application.viewmodel.UserProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@ExperimentalPermissionsApi
@Composable
fun ProfileScreen (navController: NavController, userProfileViewModel: UserProfileViewModel = viewModel()) {



        //Check user is logged in or not
        val checkUserIsNull = remember(FirebaseAuth.getInstance().currentUser?.email) {
                FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()
        };

        //Get uid from firebase
        val uid = FirebaseAuth.getInstance().uid;

        //Set current for Toast
        val context = LocalContext.current;

        //Set effect to check only user is allowed
        LaunchedEffect(checkUserIsNull, navController) {
                if (checkUserIsNull) {
                        navController.popBackStack();
                        Toast.makeText(context, "You are not logged in! So you cannot check your profile", Toast.LENGTH_LONG).show();
                }
        }

        //Set effect to fetch single user id

        LaunchedEffect(uid, userProfileViewModel) {
                userProfileViewModel.fetchSingleUserProfile(uid as String);
        }

        //Get state from user profile from view model
        val single_user = userProfileViewModel.singleUserProfile;

        Text(text = "${single_user.value?.username}")



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

