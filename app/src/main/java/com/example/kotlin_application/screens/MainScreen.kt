package com.example.kotlin_application.screens

import android.content.ContentResolver
import android.net.Uri
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Media
import android.util.Log
import android.widget.ScrollView
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import com.example.kotlin_application.ui.theme.goldYellowHex
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

    //Get uid from firebase
    val uid = FirebaseAuth.getInstance().uid;

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
        }
        ,
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
                        )
                    ,

                    //Testing location
                MenuItem(
                        id = "profile",
                        title = "Profile",
                        contentDescription = "Go to profile screen",
                        icon = Icons.Default.Image
                    ),


                    MenuItem(
                        id = "about",
                        title = "About Us",
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
                    } else {
                        MenuItem(
                            id = "Login",
                            title = "Login",
                            contentDescription = "Logout",
                            icon = Icons.Default.AccountBox
                        )

                    },
                    if(checkUserIsNull){
                        MenuItem(
                        id = "register",
                        title = "Register",
                        contentDescription = "Register",
                        icon = Icons.Default.AppRegistration
                    )} else {
                        MenuItem(
                            id = "",
                            title = "",
                            contentDescription = "Placeholder",
                        )

                    }

                )


            ) {

                //For going to the Login Page
                if (it.id == "Login") {
                    navController.navigate(
                        Screens.LoginScreen.name + "/false"
                    )
                }
                if (it.id == "Logout"){ FirebaseAuth.getInstance().signOut().run {
                    navController.navigate(
                        Screens.MainScreen.name
                    )
                }}
                if (it.id == "profile") {
                    navController.navigate(
                        Screens.ProfileScreen.name
                    )
                }
                if (it.id == "register") {
                    navController.navigate(
                        Screens.LoginScreen.name + "/true"
                    )
                }

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
                ),
                navController = navController,
                onItemClick = {
                    if(it.name == "Search"){ navController.navigate(Screens.SearchScreen.name)}
                    if(it.name == "Chat"){ navController.navigate(Screens.ChatScreen.name)}
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
                SingleForum(item = item, viewModel = viewModel, navController = navController, uid = uid)

            }
        }

    }

}



//Right now this is just used for the ForumPost screen right now. Later this button should give you an option of choosing marketplace or general
//template. !!!PLACEHOLDER!!!
@ExperimentalComposeUiApi
@Composable
fun renderFloatingButtonAction(navController: NavController) {
    val checkUserIsNull = remember(FirebaseAuth.getInstance().currentUser?.email) {
        FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()
    }
    if(!checkUserIsNull){
    FloatingActionButton(
        onClick = { navController.navigate(Screens.ForumPost.name) },
        shape = RoundedCornerShape(50.dp),
        contentColor = MaterialTheme.colors.onSecondary,
        backgroundColor = MaterialTheme.colors.onBackground
    ) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = "Go to ForumPost")
    }}
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
