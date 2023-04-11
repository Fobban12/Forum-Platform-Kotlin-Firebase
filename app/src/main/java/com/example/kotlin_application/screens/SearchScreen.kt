package com.example.kotlin_application.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter

import com.example.kotlin_application.navigation.Screens
import com.example.kotlin_application.viewmodel.ForumViewModel
import com.google.firebase.auth.FirebaseAuth

@ExperimentalComposeUiApi
@Composable
fun SearchScreen(navController: NavController ,viewModel: ForumViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {

    val state = viewModel.forum

    //Scaffold
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    //Check user is logged in or not
    val checkUserIsNull = remember(FirebaseAuth.getInstance().currentUser?.email) {
        FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()
    }

    //Get username from firebase
    val username = FirebaseAuth.getInstance().currentUser?.email?.split("@")?.get(0);

    var text = remember {
        mutableStateOf("")
    }

    var hint : String = "";
    var isHintDisplayed = remember (hint) {
        mutableStateOf(hint != "")
    }

    LaunchedEffect(text.value, viewModel) {
        viewModel.searchForums(text.value);
    }


    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {

                    IconButton(onClick = {navController.popBackStack()}) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Go Back"
                        )
                    }

                },

                title = {
                        OutlinedTextField(
                            value = text.value,
                            onValueChange = {


                                text.value = it


                            },
                            placeholder = {
                                Text(
                                    text = "Search...",
                                    color = Color.Black.copy(alpha = ContentAlpha.medium)
                                )
                            },
                            leadingIcon = {
                                          Icon(imageVector = Icons.Filled.Search,
                                              contentDescription = "Search Icon",
                                              tint = Color.Black.copy(
                                                  alpha = ContentAlpha.medium
                                              )
                                          )
                            },

                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        if (text.value.isNotEmpty()) {
                                            text.value = ""
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Close,
                                        contentDescription = "Close Icon",
                                        tint = Color.Black
                                    )
                                }
                            },

                            maxLines = 1,
                            singleLine = true,
                            modifier = Modifier.size(330.dp),
                            textStyle = TextStyle(color = Color.Black),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                backgroundColor = Color.Transparent,
                            ))



                },
                backgroundColor = MaterialTheme.colors.onBackground,
                contentColor = MaterialTheme.colors.onSecondary
            )
        }
    )

    {
        it
        LazyColumn(modifier = Modifier.padding(2.dp)) {
            items(state) {
                    item ->

                Spacer(modifier = Modifier.height(20.dp))
                Box(
                    modifier = Modifier
                        .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colors.onSurface)
                        .padding(20.dp)
                        .fillMaxWidth()

                ) {
                    Row(modifier = Modifier
                        .padding(2.dp)
                        .fillMaxHeight()) {
                        val painterState = rememberImagePainter(
                            data = "${item.image}",
                            builder = {})

                        Image(
                            painter = painterState,
                            contentDescription = "Image for forum",
                            modifier = Modifier.height(100.dp)
                        )
                        Column(modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth()) {
                            Text(text = "Title: ${item.title}", style = TextStyle(color = MaterialTheme.colors.onSecondary, fontWeight = FontWeight.Bold, fontSize = 15.sp, textAlign = TextAlign.Center), modifier = Modifier.fillMaxWidth())
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(text = "Description : ${item.description}", style = TextStyle(color = MaterialTheme.colors.onSecondary, fontWeight = FontWeight.Bold, fontSize = 12.sp, textAlign = TextAlign.Center
                            ), modifier = Modifier.fillMaxWidth())
                            Button(onClick = { navController.navigate(Screens.SingleForumScreen.name + "/${item.id}") }, modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth(), colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.onBackground,
                                contentColor = Color.White
                            )) {
                                Text(text = "Click to see more details", style = TextStyle(textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, fontSize = 10.sp), modifier = Modifier.fillMaxWidth())
                            }
                        }

                    }
                }
                Spacer(modifier = Modifier.height(20.dp))

            }
        }
    }
}






