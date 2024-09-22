package com.example.snapsphere

// sealed class to maintain a list of all the screens in the app
sealed class Screens(val route: String) {
    data object SignUpScreen: Screens("SignUpScreen")
    data object LoginInScreen: Screens("LoginScreen")
    data object FeedScreen: Screens("FeedScreen")
    data object SearchScreen: Screens("SearchScreen")
    data object MyPostsScreen: Screens("MyPostsScreen")
    data object ProfileScreen: Screens("ProfileScreen")
    data object NewPostScreen: Screens("NewPostScreen/{imageUri}") {
        // we created this function because we are sharing the uri of image so that we can show
        // the image on the NewPostScreen without uploading it on firebase and for achieving that
        // we need to pass the Uri of the image when navigating to NewPostScreen
        fun createRoute(uri: String) = "NewPostScreen/$uri"
    }
    data object SinglePostScreen: Screens("SinglePostScree")
    data object UserScreen: Screens("UserScreen")
}