package com.example.kotlin_application.screens

import android.content.ContentResolver
import android.net.Uri
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Media
import android.util.Log
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
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.kotlin_application.data.BottomNavItem
import com.example.kotlin_application.data.MenuItem
import com.example.kotlin_application.navigation.BottomNavigationBar
import com.example.kotlin_application.navigation.Drawer
import com.example.kotlin_application.navigation.DrawerBody
import com.example.kotlin_application.navigation.Screens
import com.example.kotlin_application.viewmodel.ForumViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.io.File
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import java.time.format.TextStyle

@ExperimentalCoilApi
@ExperimentalComposeUiApi
@Composable
fun MainScreen(navController: NavController, viewModel: ForumViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
//
//    LaunchedEffect(key1 = true) {
//        if (FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()) {
//            navController.navigate(Screens.LoginScreen.name)
//        } else {
//            navController.navigate(Screens.MainScreen.name)
//            Log.d("Firebase", FirebaseAuth.getInstance().currentUser?.email.toString());
//        }
//    }


    //List of forum data
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
        scaffoldState = scaffoldState,
        topBar = {
            renderTopAppBar(
                username,
                checkUserIsNull,
                navController,
                onNavigationIconClick = {
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }
                }
            )
        },
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        drawerContent = {
            Drawer()
            DrawerBody(
                items = listOf(
                    MenuItem(
                        id = "home",
                        title = "Home",
                        contentDescription = "Go to home screen",
                        icon = Icons.Default.Home
                    ),
                    MenuItem(
                        id = "settings",
                        title = "Settings",
                        contentDescription = "Go to settings screen",
                        icon = Icons.Default.Settings
                    ),
                    MenuItem(
                        id = "profile",
                        title = "Profile",
                        contentDescription = "Go to profile screen",
                        icon = Icons.Default.Image
                    ),
                    MenuItem(
                        id = "help",
                        title = "Help",
                        contentDescription = "Get help",
                        icon = Icons.Default.Info
                    ),
                    if(!checkUserIsNull){
                        MenuItem(
                            id = "Logout",
                            title = "Logout",
                            contentDescription = "Logout",
                            icon = Icons.Default.ExitToApp
                        )
                    }
                  else{
                        MenuItem(
                            id = "Login",
                            title = "Login",
                            contentDescription = "Logout",
                            icon = Icons.Default.AccountBox
                        )
                    }

                ) as List<MenuItem>


            ) {
                // when(it.id){"home"->navigateToHomeScreen
                //For going to the Login Page
                if (it.id == "Login") {
                    navController.navigate(
                        Screens.LoginScreen.name
                    )
                }
                if (it.id == "Logout"){ FirebaseAuth.getInstance().signOut().run {
                    navController.navigate(
                        Screens.MainScreen.name
                    )
                }}
                println("Clicked on ${it.title}")
            }
        },

        floatingActionButton = { renderFloatingButtonAction(navController)},
        bottomBar = {
            BottomNavigationBar(
                items = listOf(
                    BottomNavItem(
                        name = "Home",
                        route = "home",
                        icon = Icons.Default.Home
                    ),
                    BottomNavItem(
                        name = "Chat",
                        route = "chat",
                        icon = Icons.Default.Notifications,
                        badgeCount = 23
                    ),
                    BottomNavItem(
                        name = "Search",
                        route = "search",
                        icon = Icons.Default.Search,
                        badgeCount = 0
                    ),
                    //!!!!!This is just to see how the created Forum page would look like, remove this when done testing. Bao don't delete this yet.
                    //!!!!!Remind me to delete it if I forget or delete it yourself if you are sure that we have done the forum page already
                    BottomNavItem(
                        name = "ForumTest",
                        route = "Forum",
                        icon = Icons.Default.Info,
                        badgeCount = 0
                    ),
                ),
                navController = navController,
                onItemClick = {
                    if(it.name == "ForumTest"){ navController.navigate(Screens.ForumScreen.name)}
                    if(it.name == "Search"){ navController.navigate(Screens.SearchScreen.name)}
                    println("Clicked on ${it.name}")
                }
            )
        }
    ) {
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
                        .background(Color.LightGray)
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
                            Text(text = "Title: ${item.title}", style = androidx.compose.ui.text.TextStyle(color = MaterialTheme.colors.onSecondary, fontWeight = FontWeight.Bold, fontSize = 15.sp, textAlign = TextAlign.Center), modifier = Modifier.fillMaxWidth())
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(text = "Description : ${item.description}", style = androidx.compose.ui.text.TextStyle(color = MaterialTheme.colors.onSecondary, fontWeight = FontWeight.Bold, fontSize = 12.sp, textAlign = TextAlign.Center
                            ), modifier = Modifier.fillMaxWidth())
                            Button(onClick = { navController.navigate(Screens.SingleForumScreen.name + "/${item.id}") }, modifier = Modifier.padding(10.dp).fillMaxWidth(), colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.onBackground,
                                contentColor = Color.White
                            )) {
                                Text(text = "Click to see more details", style = androidx.compose.ui.text.TextStyle(textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, fontSize = 10.sp), modifier = Modifier.fillMaxWidth())
                            }
                        }

                    }
                }
                Spacer(modifier = Modifier.height(20.dp))

            }
        }
    }
}


//Right now this is just used for the ForumPost screen right now. Later this button should give you an option of choosing marketplace or general
//template. !!!PLACEHOLDER!!!
@ExperimentalComposeUiApi
@Composable
fun renderFloatingButtonAction(navController: NavController) {
    FloatingActionButton(
        onClick = { navController.navigate(Screens.ForumPost.name) },
        shape = RoundedCornerShape(50.dp),
        contentColor = MaterialTheme.colors.onSecondary,
        backgroundColor = MaterialTheme.colors.onBackground
    ) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = "Go to ForumPost")
    }
}
@ExperimentalComposeUiApi
@Composable
fun renderTopAppBar(
    username: String?,
    checkUserIsNull: Boolean,
    navController: NavController,
    onNavigationIconClick: () -> Unit
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Toggle drawer",
                )
            }
        },
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                if (!checkUserIsNull) {
                    Text(text = "Welcome $username to Agora")
                } else {
                    Text(text = "Welcome to Agora (Not Logged In)")
                }
            }
        },
        backgroundColor = MaterialTheme.colors.onBackground,
        contentColor = MaterialTheme.colors.onSecondary
    )
}
