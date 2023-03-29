package com.example.kotlin_application.navigation



enum class Screens {
    LoginScreen,
    MainScreen,
    ForumPost,
    SingleForumScreen,
    AddCommentScreen,
    CommentScreen,
    SearchScreen,
    UpdateCommentScreen,
    ChatScreen;




    companion object {
        fun fromRoute(route: String): Screens =
            when (route?.substringBefore("/")) {
                LoginScreen.name -> LoginScreen
                MainScreen.name -> MainScreen
                ForumPost.name -> ForumPost
                SingleForumScreen.name -> SingleForumScreen
                AddCommentScreen.name -> AddCommentScreen
                SearchScreen.name -> SearchScreen
                CommentScreen.name ->CommentScreen
                ChatScreen.name -> ChatScreen
                UpdateCommentScreen.name -> UpdateCommentScreen
                else -> throw java.lang.IllegalArgumentException("Route $route is not recognized")
            }
    }
}