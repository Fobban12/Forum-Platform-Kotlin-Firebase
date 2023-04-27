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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.kotlin_application.data.CommentInput
import com.example.kotlin_application.viewmodel.CommentViewModel
import com.example.kotlin_application.viewmodel.UserProfileViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth

@ExperimentalComposeUiApi
@Composable
fun AddCommentScreen (navController: NavController, forumId : String)
{

    val viewModel:CommentViewModel = viewModel()
    //Get user profile view model
    val userProfileViewModel: UserProfileViewModel = viewModel();


    //Set state for comment
    val comment = remember {
        mutableStateOf("")
    }

    val commentIsValid = remember (comment.value) {
        comment.value.trim().isNotEmpty();
    }

    val commentLengthIsValid = remember (comment.value) {
        comment.value.trim().length >=5;
    }

    //Set controller for keyboard
    val keyboardController = LocalFocusManager.current;

    //Set context for toast
    val context = LocalContext.current;

    //Get uid from firebase
    val uid = FirebaseAuth.getInstance().uid.toString();

    //Check user is null
    val checkUserIsNull = remember(FirebaseAuth.getInstance().currentUser?.email) {
        FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()
    }



    //Set effect to fetch single user id
    if(!checkUserIsNull){ LaunchedEffect(uid, checkUserIsNull, userProfileViewModel) {
        userProfileViewModel.fetchSingleUserProfile(uid);
    }
    }

    //Get state from user profile from view model
    val singleUser = userProfileViewModel.singleUserProfile;

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Go Back")
                    }
                },
                title = {
                    Text(text = "Add Comment Screen")
                },
                backgroundColor = MaterialTheme.colors.onBackground,
                contentColor = Color.White
            ) 
        }
    ) {
        it
        Surface(modifier = Modifier.fillMaxSize()) {
            Column (
                modifier = Modifier.fillMaxWidth()
                    ) {
                Spacer(modifier = Modifier.height(20.dp))

                Text(text = "Add Comment", style = androidx.compose.ui.text.TextStyle(color = MaterialTheme.colors.onBackground, fontSize = 35.sp, fontWeight = FontWeight.Bold), textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = comment.value,
                    onValueChange = {comment.value = it},
                    label = { Text(text = "Comment",
                        color = MaterialTheme.colors.onBackground)},

                    enabled = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { keyboardController.clearFocus();
                    Log.d("Comment value: ", comment.value)
                }), singleLine = false, modifier = Modifier.fillMaxWidth(), textStyle = androidx.compose.ui.text.TextStyle(
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
                    if (!commentIsValid || !commentLengthIsValid) {
                        Toast.makeText(context, "Comment is invalid and it must include at least 5 characters", Toast.LENGTH_LONG).show()
                    } else {
                        val newComment = CommentInput(comment = comment.value.trim(), createdAt = Timestamp.now(), forumId = forumId, userId = uid, username = singleUser.value?.username)
                        viewModel.saveComment(newComment, context);
                        navController.popBackStack();
                        Log.d("Successfully", "Successfully!")
                    }
                    keyboardController.clearFocus()
                },  colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.onBackground)) {
                    Text(text = "Add Comment", style = androidx.compose.ui.text.TextStyle(color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp), modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                }
            }
        }

    }
}