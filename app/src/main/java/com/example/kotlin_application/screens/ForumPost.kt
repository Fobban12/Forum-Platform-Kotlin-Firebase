package com.example.kotlin_application.screens

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kotlin_application.navigation.Screens
import com.example.kotlin_application.screens.authentication.UserForm
import com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Label


//This is for the screen to make a forum post and push the data to the database.
@ExperimentalComposeUiApi
@Composable
fun ForumPost(
    navController: NavController,
) {

//Top Bar
    Scaffold(
        topBar = {
            renderTopBar(
                navController,
                IconClick = {
                    navController.navigate(Screens.MainScreen.name)
                })
        }
    )
    {it
    Surface(modifier = Modifier
        .fillMaxSize())
      {
        Box()
        {

            Column(modifier = Modifier
                .padding(10.dp))

            {
                Text(text = "Topic Title:", modifier = Modifier.padding(bottom = 10.dp))
                TestField()
            }

        }

      }
    }






}

//For rendering the top bar.
@ExperimentalComposeUiApi
@Composable
fun renderTopBar(navController: NavController, IconClick: () -> Unit)
{

    TopAppBar(
        navigationIcon = {
            IconButton(onClick = IconClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Go Back"
                )
            }
        },
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                Text(text = "Create Forum")

            }
        },
        backgroundColor = MaterialTheme.colors.onBackground,
        contentColor = MaterialTheme.colors.onSecondary
    )

}//Here would be the text field for the title
@Composable
fun TestField() {
    var text by remember { mutableStateOf("") }

    TextField(
        value = text,
        onValueChange = { text = it },
        label = { Text("Title") }
    )
}