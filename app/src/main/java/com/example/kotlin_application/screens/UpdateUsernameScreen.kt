package com.example.kotlin_application.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.kotlin_application.navigation.Screens
import com.example.kotlin_application.viewmodel.UserProfileViewModel

@Composable
fun UpdateUsernameScreen (navController: NavController, userProfileId : String) {
    //Get user profile viewModel
    val userProfileViewModel: UserProfileViewModel = viewModel()
    //Set effect to fetch single profile
    LaunchedEffect(userProfileId, userProfileViewModel) {
        userProfileViewModel.fetchSingleUserProfileByProfileId(userProfileId = userProfileId);
    }
    
    //Get state from user profile
    val user_profile = userProfileViewModel.singleUserProfile?.value;

    //Set state for new username input
    val newUsername = remember (user_profile?.username) {
        mutableStateOf(user_profile?.username)
    }

    //Set state for check username input
    val newUsernameIsValid = remember (newUsername.value) {
        newUsername.value?.trim()?.isNotEmpty();
    };

    val newUsernameLengthIsValid = remember (newUsername.value) {
        newUsername.value?.trim()?.length?.let {
            it >= 3 // Set the minimum and maximum length of the comment
        }
    } ?: false;

    //Set context for Toast
    val context = LocalContext.current;

    //Set keyboard controller
    val keyboardController = LocalFocusManager.current;

    Scaffold(
        topBar = {TopAppBar(
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Go Back")
                }
            },
            title = {
                Text(text = "Update Username")
            },
            backgroundColor = MaterialTheme.colors.onBackground,
            contentColor = Color.White,
        )
        }
    ) {
        it
        Surface(modifier = Modifier.fillMaxSize()) {
            Column (
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(text = "Update Username", style = TextStyle(color = MaterialTheme.colors.onBackground, fontSize = 35.sp, fontWeight = FontWeight.Bold), textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(value = newUsername?.value.toString(), onValueChange = {newUsername.value = it}, label = { Text(
                    text = "Username:",
                    color = MaterialTheme.colors.onBackground
                )}, enabled = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done), keyboardActions = KeyboardActions(onDone = {
                    keyboardController.clearFocus();
                    Log.d("Username value: ", "${newUsername.value}")
                }), singleLine = false, modifier = Modifier.fillMaxWidth(), textStyle = TextStyle(
                    fontSize = 18.sp,
                    color = MaterialTheme.colors.onBackground
                ),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colors.onBackground,
                        unfocusedBorderColor = Color.Gray,
                        disabledBorderColor = Color.LightGray
                    ))
                Spacer(modifier = Modifier.height(50.dp))
                Button(onClick = {
                    if (!newUsernameIsValid!! || !newUsernameLengthIsValid) {
                        Toast.makeText(context, "New username is invalid and it must include at least 5 characters", Toast.LENGTH_LONG).show()
                    } else {
                        userProfileViewModel.updateUsername(user_profile?.id as String, newUsername.value as String, context = context, user_profile?.userId as String );
                        navController.navigate(Screens.ProfileScreen.name)
                        Log.d("Successfully", "Successfully!")
                    }
                    keyboardController.clearFocus()
                },  colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.onBackground) ) {
                    Text(text = "Update Username", style = TextStyle(color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp), modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                }
            }
        }
    }
}