package com.example.kotlin_application.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.kotlin_application.data.BottomNavItem
import com.example.kotlin_application.data.MenuItem
import com.example.kotlin_application.navigation.BottomNavigationBar
import com.example.kotlin_application.navigation.Drawer
import com.example.kotlin_application.navigation.DrawerBody
import com.example.kotlin_application.navigation.Screens
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@Composable
fun MainScreen(navController: NavController) {
//
//    LaunchedEffect(key1 = true) {
//        if (FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()) {
//            navController.navigate(Screens.LoginScreen.name)
//        } else {
//            navController.navigate(Screens.MainScreen.name)
//            Log.d("Firebase", FirebaseAuth.getInstance().currentUser?.email.toString());
//        }
//    }
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val checkUserIsNull = remember(FirebaseAuth.getInstance().currentUser?.email) {
        FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()
    }

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
                    // Placeholder Login, Bao put this in the correct place later, or ask me to if you don't want to.
                    // Or delete this if you have idea where to place it.
                    MenuItem(
                        id = "Login",
                        title = "Login",
                        contentDescription = "LogIn",
                        icon = Icons.Default.Build
                    ),
                    MenuItem(
                        id = "Forum",
                        title = "Forum",
                        contentDescription = "Forum",
                        icon = Icons.Default.Forum
                    ),
                    )

            ) {
                // when(it.id){"home"->navigateToHomeScreen
                //For going to the Login Page
                if (it.id == "Login") {
                    navController.navigate(
                        Screens.LoginScreen.name
                    )
                } else if (it.id == "Forum") {
                    navController.navigate(
                        Screens.ForumScreen.name
                    )
                }
                println("Clicked on ${it.title}")
            }
        },

        floatingActionButton = { renderFloatingButtonAction()},
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
                    println("Clicked on ${it.name}")
                }
            )
        }
    ) {
        it
    }
}

@ExperimentalComposeUiApi
@Composable
fun renderFloatingButtonAction() {
    FloatingActionButton(
        onClick = { Log.d("Add item", "To Add Item Page") },
        shape = RoundedCornerShape(50.dp),
        contentColor = MaterialTheme.colors.onSecondary,
        backgroundColor = MaterialTheme.colors.onBackground
    ) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = "Add item")
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
        actions = {
            if (!checkUserIsNull) {
                IconButton(
                    onClick = {
                        FirebaseAuth.getInstance().signOut().run {
                            navController.navigate(
                                Screens.LoginScreen.name
                            )
                        }
                    }
                ) {
                    Icon(imageVector = Icons.Filled.Logout, contentDescription = "Log Out")
                }
            }
        },

        backgroundColor = MaterialTheme.colors.onBackground,
        contentColor = MaterialTheme.colors.onSecondary
    )
}
