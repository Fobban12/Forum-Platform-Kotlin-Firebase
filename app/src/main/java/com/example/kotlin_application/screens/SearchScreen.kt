package com.example.kotlin_application.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.kotlin_application.data.Forum
import com.example.kotlin_application.data.ProductList
import com.example.kotlin_application.navigation.Screens
import com.example.kotlin_application.viewmodel.ForumViewModel
import com.google.firebase.auth.FirebaseAuth

@ExperimentalComposeUiApi
@Composable
fun SearchScreen(navController: NavController ,viewModel: ForumViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {

    val state = viewModel.forum;


    //Scaffold
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    //Check user is logged in or not
    val checkUserIsNull = remember(FirebaseAuth.getInstance().currentUser?.email) {
        FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()
    }

    //Get username from firebase
    val username = FirebaseAuth.getInstance().currentUser?.email?.split("@")?.get(0)


    Scaffold(
        topBar = {
            SearchTopBar(
                navController,
                IconClick = {
                    navController.navigate(Screens.MainScreen.name)
                })
        }
    )

    {
        it
        //Test with dummy data from firestore. Will complete when Jere completes Post for Forum
        //        Fetch single forum data with LazyColumn
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






@ExperimentalComposeUiApi
@Composable
fun SearchTopBar(navController: NavController, IconClick: () -> Unit) {
    val scaffoldState = rememberScaffoldState();

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

              SearchBar(
                   hint = " ",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 0.dp, end = 45.dp)

                )
            }
        },
        backgroundColor = MaterialTheme.colors.onBackground,
        contentColor = MaterialTheme.colors.onSecondary
    )


}





@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "",
    onSearch: (String) -> Unit = {},
) {
    var text by remember {
        mutableStateOf("")
    }
    var isHintDisplayed by remember {
        mutableStateOf(hint != "")
    }

    Box(modifier = modifier) {
        BasicTextField(
            value = text,
            onValueChange = {
                text = it
                onSearch(it)
            },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp)

                    )
                    if (isHintDisplayed) {
                        Text(
                            text = hint,
                            color = Color.LightGray,
                            modifier = Modifier
                                .padding(horizontal = 20.dp, vertical = 12.dp)
                        )
                    }
                }
    }

