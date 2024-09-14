package com.example.snapsphere

// sealed class to maintain a list of all the screens in the app
sealed class Screens(val route: String) {
    data object SignUpScreen: Screens("SignUpScreen")
    data object LoginInScreen: Screens("LoginScreen")
    data object FeedScreen: Screens("FeedScreen")
    data object SearchScreen: Screens("SearchScreen")
    data object MyPostsScreen: Screens("MyPostsScreen")
}