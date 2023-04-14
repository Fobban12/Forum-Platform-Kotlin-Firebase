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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.kotlin_application.data.CommentInput
import com.example.kotlin_application.navigation.Screens
import com.example.kotlin_application.viewmodel.CommentViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth


@ExperimentalComposeUiApi
@Composable
fun UpdateCommentScreen (navController: NavController, commentId : String ) {

    //Get comment viewModel
    val commentViewModel: CommentViewModel = viewModel()

    //Set controller for keyboard
    val keyboardController = LocalFocusManager.current;

    //Set context for toast
    val context = LocalContext.current;

    //Get username from firebase
    val username = FirebaseAuth.getInstance().currentUser?.email?.split("@")?.get(0)

    //Get uid from firebase
    val uid = FirebaseAuth.getInstance().uid.toString();

    //Set effect to get single comment
    LaunchedEffect(commentId, commentViewModel) {
        commentViewModel.getSingleComment(commentId);
    }

    val singleComment = commentViewModel?.singleComment?.value;

    //Set state for comment
    var comment = remember (singleComment?.comment) {
        mutableStateOf(singleComment?.comment);
    }

    val commentIsValid = remember (comment.value) {
        comment.value?.trim()?.isNotEmpty();
    };

    val commentLengthIsValid = remember(comment.value) {
        comment.value?.trim()?.length?.let {
            it >= 5 // Set the minimum and maximum length of the comment
        }
    } ?: false;

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screens.MainScreen.name
                    ) }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back Button")
                    }
                },
                title = {
                    Text(text = "Update Comment")
                },
                backgroundColor = MaterialTheme.colors.onBackground,
                contentColor = Color.White
            )
        }) {
        it

        Surface(modifier = Modifier.fillMaxSize()) {
            Column (
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(text = "Update Comment", style = TextStyle(color = MaterialTheme.colors.onBackground, fontSize = 35.sp, fontWeight = FontWeight.Bold), textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(value = comment?.value.toString(), onValueChange = {comment.value = it}, label = { Text(
                    text = "Comment",
                    color = MaterialTheme.colors.onBackground
                )}, enabled = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done), keyboardActions = KeyboardActions(onDone = {
                    keyboardController.clearFocus();
                    Log.d("Comment value: ", "${comment.value}")
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
                    if (!commentIsValid!! || !commentLengthIsValid) {
                        Toast.makeText(context, "Comment is invalid and it must include at least 5 characters", Toast.LENGTH_LONG).show()
                    } else {
                        commentViewModel.updateComment(commentId, comment?.value?.trim().toString(), context = context);
                        navController.popBackStack();
                        Log.d("Successfully", "Successfully!")
                    }
                    keyboardController.clearFocus()
                },  colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.onBackground) ) {
                    Text(text = "Update Comment", style = TextStyle(color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp), modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                }
            }
        }
    }
}