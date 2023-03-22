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
                    )
                ),
                onItemClick = {
                    // when(it.id){"home"->navigateToHomeScreen
                    println("Clicked on ${it.title}")
                }
            )
        },
        floatingActionButton = { renderFloatingButtonAction() },
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
                        name = "Settings",
                        route = "settings",
                        icon = Icons.Default.Settings,
                        badgeCount = 214
                    ),
                ),
                navController = navController,
                onItemClick = {
                    navController.navigate(it.route)
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
                    contentDescription = "Toggle drawer"
                )
            }
        },
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                if (!checkUserIsNull) {
                    Text(text = "Hello $username")
                } else {
                    Text(text = "")
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
