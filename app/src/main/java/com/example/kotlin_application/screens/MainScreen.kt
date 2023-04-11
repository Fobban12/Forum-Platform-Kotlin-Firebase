package com.example.kotlin_application.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
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
import coil.annotation.ExperimentalCoilApi
import com.example.kotlin_application.viewmodel.UserProfileViewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable


@ExperimentalCoilApi
@ExperimentalComposeUiApi
@Composable
fun MainScreen(navController: NavController,
               viewModel: ForumViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
               userProfileViewModel: UserProfileViewModel = androidx.lifecycle.viewmodel.compose.viewModel()

) {

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
    val uid = FirebaseAuth.getInstance().uid.toString();




    //Set effect to set username for title bar
    //Set effect to fetch single user id

    LaunchedEffect(uid, userProfileViewModel, checkUserIsNull) {
        userProfileViewModel.fetchSingleUserProfile(uid);
    }

    //Get state from user profile from view model
//    val single_user = userProfileViewModel.singleUserProfile;

    val single_user = userProfileViewModel.singleUserProfile.observeAsState();



    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            renderTopAppBar(
                single_user.value?.username,
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

                    if(!checkUserIsNull){ MenuItem(

                        id = "profile",
                        title = "Profile",
                        contentDescription = "Go to profile screen",
                        icon = Icons.Default.Image
                    )} else (
                            MenuItem(
                                id = "register",
                                title = "Register",
                                contentDescription = "Register",
                                icon = Icons.Default.AppRegistration
                            )),

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
                    MenuItem(
                        id = "settings",
                        title = "Settings",
                        contentDescription = "Go to settings screen",
                        icon = Icons.Default.Settings
                    ),
                    MenuItem(
                        id = "about",
                        title = "About Us",
                        contentDescription = "Get help",
                        icon = Icons.Default.Info
                    ),

                    )


            ) {

                //For going to the Login Page
                if (it.id == "home") {
                    navController.navigate(
                        Screens.MainScreen.name
                    )
                }
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

            }
        },

        floatingActionButton = { renderFloatingButtonAction(navController)},
        bottomBar = {
            Modifier.padding(top = 100.dp)
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
                        icon = Icons.Default.Chat,
                        badgeCount = 0
                    ),
                    BottomNavItem(
                        name = "Search",
                        route = "search",
                        icon = Icons.Default.Search,
                        badgeCount = 0
                    )
                ),
                navController = navController,
                onItemClick = {
                    if(it.name == "Search"){ navController.navigate(Screens.SearchScreen.name)}
                    if(it.name == "Chat"){ navController.navigate(Screens.ChatScreen.name)}
                    if(it.name == "Home"){ navController.navigate(Screens.MainScreen.name)}
                }
            )
        }
    ) {
        it

        //Fetch single forum data with LazyColumn
            LazyColumn(modifier = Modifier
                .padding(2.dp)
                .padding(it)

            ) {
                items(state) {
                        item ->
                    SingleForum(item = item, viewModel = viewModel, navController = navController, uid = uid)
                    Spacer(modifier = Modifier.height(20.dp))

                }
            }
        




    }

}

//Right now this is just used for the ForumPost screen right now. Later this button should give you an option of choosing marketplace or general
//Marketplace is done but Forum template still needs to be done.
//Two buttons will be done when the other template is.
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
