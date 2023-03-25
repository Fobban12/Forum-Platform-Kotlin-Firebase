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
import androidx.navigation.NavController
import java.time.format.TextStyle

@ExperimentalComposeUiApi
@Composable
fun AddCommentScreen (navController: NavController) {

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
                contentColor = MaterialTheme.colors.onSecondary
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
                OutlinedTextField(value = comment.value, onValueChange = {comment.value = it}, label = { Text(
                    text = "Comment",
                    color = MaterialTheme.colors.onBackground
                )}, enabled = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done), keyboardActions = KeyboardActions(onDone = {
                    keyboardController.clearFocus();
                    Log.d("Comment value: ", "${comment.value}")
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
                        Log.d("Successfully", "Successfully!")
                    }
                    keyboardController.clearFocus()
                },  colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.onBackground) ) {
                    Text(text = "Add Comment", style = androidx.compose.ui.text.TextStyle(color = MaterialTheme.colors.onSecondary, fontWeight = FontWeight.Bold, fontSize = 20.sp), modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                }
            }
        }

    }
}