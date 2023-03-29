package com.example.kotlin_application.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kotlin_application.screens.*
import com.example.kotlin_application.screens.authentication.LoginScreen

@ExperimentalComposeUiApi
@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screens.MainScreen.name) {
        composable(Screens.LoginScreen.name) {
            LoginScreen(navController = navController)
        }
        composable(Screens.MainScreen.name) {
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