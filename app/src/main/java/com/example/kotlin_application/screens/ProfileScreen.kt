package com.example.kotlin_application.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract.Profile
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.kotlin_application.data.Forum
import com.example.kotlin_application.data.UserProfile
import com.example.kotlin_application.navigation.Screens
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.example.kotlin_application.ui.theme.CameraXComposeTheme
import com.example.kotlin_application.ui.theme.goldYellowHex
import com.example.kotlin_application.ui.theme.neonGreen
import com.example.kotlin_application.viewmodel.UserProfileViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

@ExperimentalPermissionsApi
@Composable
fun ProfileScreen (navController: NavController, userProfileViewModel: UserProfileViewModel = viewModel()) {

        //Set image height based on screen height
        val screenHeight = LocalConfiguration.current.screenHeightDp.dp
        val boxHeight = with(LocalDensity.current) { screenHeight * 0.3f }

        //Check user is logged in or not
        val checkUserIsNull = remember(FirebaseAuth.getInstance().currentUser?.email) {
                FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()
        };

        //Get uid and username from firebase
        val uid = FirebaseAuth.getInstance().uid;
        val username = FirebaseAuth.getInstance().currentUser?.email?.split("@")?.get(0)

        //Set current for Toast
        val context = LocalContext.current;

        //Set config for screen
        val configuration = LocalConfiguration.current

        //Expanded for top bar drop down
        var expanded = remember {
                mutableStateOf(false)
        };


        val screenWidth = configuration.screenWidthDp.dp
        val displayMetrics = context.resources.displayMetrics
        val screenWidthInPixels = displayMetrics.widthPixels.toFloat()


        //Set effect to fetch single user id

        LaunchedEffect(uid, checkUserIsNull, userProfileViewModel) {
                userProfileViewModel.fetchSingleUserProfile(uid as String);
        }

        //Get state from user profile from view model
        val single_user = userProfileViewModel.singleUserProfile;

        //Image stuff
        var imageUri by remember { mutableStateOf<Uri?>(null) }
        val bitmap = remember { mutableStateOf<Bitmap?>(null) }
        val launcher =
                rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
                        imageUri = uri
                }
        //Get storage reference
        val ref: StorageReference = FirebaseStorage.getInstance().reference;


        //Set launch effect
        LaunchedEffect(imageUri, single_user.value?.id) {
                imageUri?.let {
                        ref.child("/users/$username/profile/profilePic/image")
                                .putFile(it).addOnSuccessListener {
                                        val urlDownload =
                                                ref.child("/users/$username/profile/profilePic/image").downloadUrl
                                        urlDownload.addOnSuccessListener {

                                                userProfileViewModel.updateImage(
                                                        single_user.value?.id as String,
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

        Scaffold(
                topBar = {
                        TopAppBar(
                                navigationIcon = {
                                        IconButton(onClick = {  navController.navigate(Screens.MainScreen.name) }) {
                                                Icon(
                                                        imageVector = Icons.Default.ArrowBack,
                                                        contentDescription = "Go Back"
                                                )
                                        }
                                },
                                title = {
                                        Text(text = "User Profile Screen")
                                },
                                backgroundColor = MaterialTheme.colors.onBackground,
                                contentColor = Color.White,
                                actions = {
                                        IconButton(onClick = { expanded.value = !expanded.value }) {
                                                Icon(
                                                        imageVector = Icons.Filled.MoreVert,
                                                        contentDescription = null
                                                )
                                        }
                                        DropdownMenu(
                                                expanded = expanded.value,
                                                onDismissRequest = { expanded.value = false }) {
                                                DropdownMenuItem(onClick = {
                                                        expanded.value =
                                                                false; navController.navigate(
                                                        Screens.UpdateUsernameScreen.name + "/${single_user.value?.id}"
                                                )
                                                }) {
                                                        Text("Update username")
                                                }

                                        }
                                }
                        )
                },
        ) {
                it
                Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                ) {
                        Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                        .height(boxHeight)
                                        .width(boxHeight)
                                        .padding(3.dp)
                                        .clip(shape = CircleShape)
                                        .border(
                                                width = 10.dp,
                                                color = MaterialTheme.colors.onBackground,
                                                shape = CircleShape
                                        )
                                        .clickable {


                                                launcher.launch("image/*")


                                        }
                        ) {
                                Surface(
                                        modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(5.dp),
                                ) {

                                        Column() {
                                                Column(
                                                        verticalArrangement = Arrangement.Center,
                                                        horizontalAlignment = Alignment.CenterHorizontally,
                                                        modifier = Modifier.fillMaxWidth()
                                                ) {
                                                        if (single_user.value?.image != null || single_user.value?.image.toString().isNotEmpty()) {


                                                                val painterState = rememberImagePainter(
                                                                        data = "${single_user.value?.image}",
                                                                        builder = {})
                                                        
                                                                Image(
                                                                        painter = painterState,
                                                                        contentDescription = "Image for forum",
                                                                        modifier = Modifier.fillMaxSize()
                                                                )


                                                        } 
                                                }



                                        }
                                }
                        }
                        Spacer(modifier = Modifier.height(30.dp))
                        Text(text = "No avatar upload yet or want to upload new image! Please click on the circle", modifier = Modifier.padding(10.dp),  style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp, textAlign = TextAlign.Center))
                        Spacer(modifier = Modifier.height(30.dp))
                        Text(
                                text = "Username: ${single_user.value?.username}",
                                modifier = Modifier.padding(10.dp),
                                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        )
                }
        }



}

