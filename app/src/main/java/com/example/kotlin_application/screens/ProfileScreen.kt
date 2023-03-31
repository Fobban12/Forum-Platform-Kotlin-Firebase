package com.example.kotlin_application.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.example.kotlin_application.ui.theme.CameraXComposeTheme
import com.example.kotlin_application.ui.theme.goldYellowHex
import com.example.kotlin_application.ui.theme.lightGray
import com.example.kotlin_application.viewmodel.UserProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

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

        //Get uid from firebase
        val uid = FirebaseAuth.getInstance().uid;

        //Set current for Toast
        val context = LocalContext.current;

        //Set confid for screen
        val configuration = LocalConfiguration.current

        val screenWidth = configuration.screenWidthDp.dp
        val displayMetrics = context.resources.displayMetrics
        val screenWidthInPixels = displayMetrics.widthPixels.toFloat()

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


                Column(
                        modifier = Modifier.fillMaxSize(),
                        back
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                ) {
                        Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                        .height(boxHeight)
                                        .width(boxHeight)
                                        .padding(5.dp)
                                        .clip(shape = CircleShape)
                                        .border(
                                                width = 1.dp,
                                                color = Color.Gray,
                                                shape = CircleShape
                                        )
                        ) {
                                Surface(
                                        modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(5.dp)
                                ) {

                                        Column() {
                                                Column(
                                                        modifier = Modifier.fillMaxWidth().background(color = lightGray),
                                                        verticalArrangement = Arrangement.Center,
                                                        horizontalAlignment = Alignment.CenterHorizontally
                                                ) {
                                                        if (single_user.value?.image != null) {
                                                                Text(
                                                                        text = "No Image Yet",
                                                                        style = TextStyle(
                                                                                fontSize = 15.sp,
                                                                                fontWeight = FontWeight.Bold
                                                                        )
                                                                );
                                                        } else {
                                                                Text(text = "Image")
                                                        }
                                                }
                                        }


                                }
                        }
                        Spacer(modifier = Modifier.height(30.dp))
                        Text(
                                text = "Username: ${single_user.value?.username}",
                                modifier = Modifier.padding(10.dp),
                                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        )
                }


}

