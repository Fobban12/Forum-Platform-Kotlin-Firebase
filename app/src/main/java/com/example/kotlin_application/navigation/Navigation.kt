package com.example.kotlin_application.navigation

import android.net.Uri
import android.os.Parcelable
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.versionedparcelable.VersionedParcelize
import com.example.kotlin_application.screens.*
import com.example.kotlin_application.screens.authentication.LogScreen
import com.example.kotlin_application.viewmodel.UserProfileViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.firebase.auth.FirebaseAuth



@ExperimentalPermissionsApi
@ExperimentalComposeUiApi
@Composable
fun Navigation(userProfileViewModel: UserProfileViewModel = viewModel()) {
    val navController = rememberNavController()

    val uid = FirebaseAuth.getInstance().uid.toString();

    NavHost(navController = navController, startDestination = Screens.MainScreen.name) {
        val loginScreen = Screens.LoginScreen.name;
        composable("$loginScreen/{isRegister}", arguments = listOf(navArgument("isRegister") {
            type = NavType.StringType
            nullable = true
        })) { backStackEntry ->
            backStackEntry.arguments?.getString("isRegister").let {
                LogScreen(navController = navController, isRegister = it.toString())
            }
        }
//        composable(Screens.LoginScreen.name) {
//            LoginScreen(navController = navController)
//        }
        composable(Screens.MainScreen.name) {

            LaunchedEffect(uid, userProfileViewModel) {
                userProfileViewModel.fetchSingleUserProfile(uid)
            }
            MainScreen(navController = navController)
        }

        composable(Screens.ForumPost.name) {
            ForumPost(navController = navController)
        }
        composable(Screens.SearchScreen.name) {
            SearchScreen(navController = navController)
        }
        
        composable(Screens.ChatScreen.name){
            ChatScreen(navController = navController)
        }

        composable(Screens.ProfileScreen.name) {
            ProfileScreen(navController = navController)
        }
        
        composable(Screens.ChatListScreen.name) {
            ChatListScreen(navController = navController)
        }

        val cameraScreen = Screens.CameraScreen.name;
        composable("$cameraScreen/{userProfileId}", arguments = listOf(navArgument("userProfileId") {
            type = NavType.StringType
        })) { backStackEntry ->
            backStackEntry.arguments?.getString("userProfileId").let {
                CameraScreen(navController = navController, userProfileId = it.toString())
            }
        }


        val contactScreen = Screens.ContactScreen.name;
        composable("$contactScreen/{chatId}/{userIds}", arguments = listOf(navArgument("userIds") {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        }, navArgument("chatId") {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        }
        )) { backStackEntry ->

            val strings = backStackEntry.arguments?.getString("userIds")?.replace("[", "")?.replace("]", "")?.split(",");

            val chatId = backStackEntry.arguments?.getString("chatId");
            ContactScreen(navController = navController, userIds = strings, chatId = chatId);
        }
        

        val updateUserNameScreen = Screens.UpdateUsernameScreen.name
        composable("$updateUserNameScreen/{userProfileId}", arguments = listOf(navArgument("userProfileId") {
            type = NavType.StringType
            nullable = true
        })) { NavBackStackEntry ->
            NavBackStackEntry.arguments?.getString("userProfileId").let {
                UpdateUsernameScreen(navController = navController, userProfileId = it.toString())
            }
        }

        

        val updateCommentScreenName = Screens.UpdateCommentScreen.name;
        composable("$updateCommentScreenName/{commentId}", arguments = listOf(navArgument("commentId") {
            type = NavType.StringType
            nullable = true
        })) { backStackEntry ->
            backStackEntry.arguments?.getString("commentId").let {
                UpdateCommentScreen(navController = navController, commentId = it.toString())
            }
        }

        val detailForumName = Screens.SingleForumScreen.name
        composable("$detailForumName/{forumId}", arguments = listOf(navArgument("forumId") {
            type = NavType.StringType
        })) { backStackEntry ->  
            backStackEntry.arguments?.getString("forumId").let { 
                SingleForumScreen(navController = navController, forumId = it.toString())
            }
        }
    
        val addCommentScreenName = Screens.AddCommentScreen.name
        composable("$addCommentScreenName/{forumId}", arguments = listOf(navArgument("forumId") {
            type = NavType.StringType
        })) { backStackEntry -> 
            backStackEntry.arguments?.getString("forumId").let { 
                AddCommentScreen(navController = navController, forumId = it.toString())
            }
        }

        val commentScreenName = Screens.CommentScreen.name
        composable("$commentScreenName/{forumId}", arguments = listOf(navArgument("forumId") {
            type = NavType.StringType
        })) {backStackEntry ->
            backStackEntry.arguments?.getString("forumId").let {
                CommentScreen(navController = navController, forumId = it.toString())
            }
        }





    }
}