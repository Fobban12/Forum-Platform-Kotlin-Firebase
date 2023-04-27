package com.example.kotlin_application.screens

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.kotlin_application.navigation.Screens
import com.example.kotlin_application.ui.theme.goldYellowHex
import com.example.kotlin_application.viewmodel.UserProfileViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

@ExperimentalPermissionsApi
@Composable
fun ProfileScreen (navController: NavController) {

        //Get user profile view model
        val userProfileViewModel: UserProfileViewModel = viewModel();

        //Set image height based on screen height
        val screenHeight = LocalConfiguration.current.screenHeightDp.dp
        val boxHeight = with(LocalDensity.current) { screenHeight * 0.3f }

        //Check user is logged in or not
        val checkUserIsNull = remember(FirebaseAuth.getInstance().currentUser?.email) {
                FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()
        };

        //Get uid and username from firebase
        val uid = FirebaseAuth.getInstance().uid;

        //Get current user
        val user = FirebaseAuth.getInstance().currentUser
        //Set current for Toast
        val context = LocalContext.current;



        //Expanded for top bar drop down
        var expanded = remember {
                mutableStateOf(false)
        };

        //Set effect to check user logged in to access this screen
        LaunchedEffect(checkUserIsNull) {
                if (checkUserIsNull) {
                        navController.navigate(Screens.MainScreen.name)
                }
        }


        //Set effect to fetch single user id
        if(!checkUserIsNull) {
                LaunchedEffect(uid, checkUserIsNull, userProfileViewModel) {
                        userProfileViewModel.fetchSingleUserProfile(uid as String);
                }
        }
        //Get state from user profile from view model
        val single_user = userProfileViewModel.singleUserProfile;
        val user_Id = single_user.value?.userId

        //Image stuff
        var imageUri by remember { mutableStateOf<Uri?>(null) }
        val launcher =
                rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
                        imageUri = uri
                }
        //Get storage reference
        val ref: StorageReference = FirebaseStorage.getInstance().reference;

        //Set dialog boolean
        var showDialog = remember { mutableStateOf(false) }

        var showDialogDelete = remember { mutableStateOf(false) }

        //Set width based on screen width
        val screenWidth = LocalConfiguration.current.screenWidthDp.dp
        val buttonModalWidth = with(LocalDensity.current) { screenWidth * 0.8f }



        //Set launch effect
        LaunchedEffect(imageUri, single_user.value?.id) {
                imageUri?.let { it ->
                        ref.child("/users/${single_user.value?.userId}/profile/profilePic/image")
                                .putFile(it).addOnSuccessListener {
                                        val urlDownload =
                                                ref.child("/users/${single_user.value?.userId}/profile/profilePic/image").downloadUrl
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
                        verticalArrangement = Arrangement.Center,
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

                                                showDialog.value = true;
//                                                launcher.launch("image/*")


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
                        Text(text = "No avatar uploaded yet? Click on the circle to add or update", modifier = Modifier.padding(10.dp),  style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp, textAlign = TextAlign.Center))
                        Spacer(modifier = Modifier.height(30.dp))
                        Text(
                                text = "Username: ${single_user.value?.username}",
                                modifier = Modifier.padding(10.dp),
                                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        )
                        Button(onClick = { navController.navigate(Screens.ChatListScreen.name) }, colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.onBackground)) {
                                Text(text = "Click to move to chat list!", style = TextStyle(color = goldYellowHex, fontWeight = FontWeight.Bold, fontSize = 18.sp))
                        }
                        Button(onClick = { showDialogDelete.value = true }, colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.onBackground)) {
                                Text(text = "Delete account", style = TextStyle(color = goldYellowHex, fontWeight = FontWeight.Bold, fontSize = 18.sp))
                        }

                }
        }

        if (showDialog.value) {
                AlertDialog(
                        onDismissRequest = { showDialog.value = false },
                        title = { Text(text = "Choose an option", style = TextStyle(fontWeight = FontWeight.Bold)) },
                        text = { Text(text = "Select one of the following options", style = TextStyle(fontWeight = FontWeight.Bold)) },
                        buttons = {
                                Column(modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally) {
                                        Button(onClick = { showDialog.value = false}, colors = ButtonDefaults.buttonColors(
                                                backgroundColor = MaterialTheme.colors.onBackground,
                                                contentColor = Color.White), modifier = Modifier.width(buttonModalWidth)) {
                                                Text(text = "Cancel", style = TextStyle(color = goldYellowHex, fontWeight = FontWeight.Bold))
                                        }
                                        Button(onClick = { showDialog.value = false;  launcher.launch("image/*"); }, colors = ButtonDefaults.buttonColors(
                                                backgroundColor = MaterialTheme.colors.onBackground,
                                                contentColor = Color.White), modifier = Modifier.width(buttonModalWidth)) {
                                                Text(text = "Upload Image", style = TextStyle(color = goldYellowHex, fontWeight = FontWeight.Bold))
                                        }
                                        Button(onClick = { showDialog.value = false;
                                                navController.navigate(Screens.CameraScreen.name + "/${single_user.value?.id}")
                                                 }, colors = ButtonDefaults.buttonColors(
                                                backgroundColor = MaterialTheme.colors.onBackground,
                                                contentColor = Color.White), modifier = Modifier.width(buttonModalWidth)) {
                                                Text(text = "Take a photo", style = TextStyle(color = goldYellowHex, fontWeight = FontWeight.Bold))
                                        }
                                }

                        }
                )
        }

        if (showDialogDelete.value) {
                AlertDialog(
                        onDismissRequest = { showDialogDelete.value = false },
                        backgroundColor = Color.LightGray,
                        title = {
                                Text(text = "Delete account", style = TextStyle(textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, fontSize = 16.sp))
                        },
                        text = {
                                Text(text = "Are you sure you want to delete your account? This choice will be permanent", style = TextStyle(color = MaterialTheme.colors.onBackground,textAlign = TextAlign.Left, fontWeight = FontWeight.Bold, fontSize = 14.sp))
                        },
                        confirmButton = {
                                Button(onClick = { showDialogDelete.value = false; user?.delete()?.addOnCompleteListener { task -> if(task.isSuccessful)
                                {
                                        userProfileViewModel.deleteUser(user_Id as String)
                                        Toast.makeText(context, "Account deleted", Toast.LENGTH_LONG).show()
                                        navController.navigate(Screens.MainScreen.name)
                                }
                                        else {
                                        Toast.makeText(context, "Too long since last Login, please Login and try once again.", Toast.LENGTH_LONG).show()
                                        }

                                }

                                }, colors = ButtonDefaults.buttonColors(
                                        backgroundColor = MaterialTheme.colors.onBackground,
                                        contentColor = Color.White)) {
                                        Text(text = "Yes (This choice is permanent)")
                                }
                        },
                        dismissButton = {
                                Button(onClick = { showDialogDelete.value = false }, colors = ButtonDefaults.buttonColors(
                                        backgroundColor = MaterialTheme.colors.onBackground,
                                        contentColor = Color.White)) {
                                        Text(text = "No")
                                }
                        }
                )

        }

}




