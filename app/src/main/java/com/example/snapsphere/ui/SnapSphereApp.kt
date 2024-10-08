package com.example.snapsphere.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.snapsphere.Screens
import com.example.snapsphere.auth.LoginScreen
import com.example.snapsphere.data.PostData
import com.example.snapsphere.data.UserData
import com.example.snapsphere.ui.screens.AboutScreen
import com.example.snapsphere.ui.screens.FeedScreen
import com.example.snapsphere.ui.screens.MyPostsScreen
import com.example.snapsphere.ui.screens.NewPostScreen
import com.example.snapsphere.ui.screens.ProfileScreen
import com.example.snapsphere.ui.screens.SearchScreen
import com.example.snapsphere.ui.screens.SinglePostScreen
import com.example.snapsphere.ui.screens.UserScreen
import com.example.snapsphere.viewmodel.IgViewModel

@Composable
fun SnapSphereApp(
    igViewModel: IgViewModel,
    modifier: Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screens.FeedScreen.route,
        enterTransition = {
            fadeIn(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = EaseIn
                )
            )
        },
        exitTransition = {
            fadeOut(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = EaseOut
                )
            )
        }
    ) {
        // feed screen
        composable(Screens.FeedScreen.route) {
            FeedScreen(
                igViewModel = igViewModel,
                navigateToScreen = { screen: Screens ->
                    navController.navigate(screen.route) {
                        popUpTo(0)
                    }
                },
                navigateToUserProfile = {   userData: UserData, followers: Int ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("userData", userData)
                    navController.currentBackStackEntry?.savedStateHandle?.set("followers", followers)
                    navController.navigate(Screens.UserScreen.route)
                }
            )
        }

        // search screen
        composable(Screens.SearchScreen.route) {
            BackHandler {
                navController.navigate(Screens.FeedScreen.route) {
                    popUpTo(0)
                }
            }
            SearchScreen(
                igViewModel = igViewModel,
                navigateToScreen = { screen: Screens ->
                    navController.navigate(screen.route) {
                        popUpTo(0)
                    }
                },
                modifier = modifier,
                onPostClick = {     post: PostData ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("postData", post)
                    navController.navigate(Screens.SinglePostScreen.route)
                },
                onUserProfileClick = { user: UserData ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("userData", user)
                    navController.navigate(Screens.UserScreen.route)
                }
            )
        }

        // my posts screen
        composable(Screens.MyPostsScreen.route) {
            BackHandler {
                navController.navigate(Screens.FeedScreen.route) {
                    popUpTo(0)
                }
            }
            MyPostsScreen(
                igViewModel = igViewModel,
                navigateToScreen = { screen: Screens ->
                    navController.navigate(screen.route) {
                        popUpTo(0)
                    }
                },
                goToProfileScreen = {
                    navController.navigate(Screens.ProfileScreen.route)
                },
                navigateToNewPostScreen = { route ->
                    navController.navigate(route)
                },
                onPostClick = { post: PostData ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("postData", post)
                    navController.navigate(Screens.SinglePostScreen.route)
                },
                goToAboutScreen = {
                    navController.navigate(Screens.AboutScreen.route)
                }
            )
        }

        // single post screen
        composable(Screens.SinglePostScreen.route) {
            val postData = navController.previousBackStackEntry?.savedStateHandle?.get<PostData>("postData") ?: PostData()
            SinglePostScreen(
                igViewModel = igViewModel,
                postData = postData,
                onBack = {
                    navController.popBackStack()
                },
                navigateToUserProfile = {   userData: UserData, followers: Int ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("userData", userData)
                    navController.currentBackStackEntry?.savedStateHandle?.set("followers", followers)
                    navController.navigate(Screens.UserScreen.route)
                },
                navigateToScreen = { screen: Screens ->
                    navController.navigate(screen.route) {
                        popUpTo(0)
                    }
                }
            )
        }

        // another user screen
        composable(Screens.UserScreen.route) {
            val userData = navController.previousBackStackEntry?.savedStateHandle?.get<UserData>("userData") ?: UserData()
            val followers = navController.previousBackStackEntry?.savedStateHandle?.get<Int>("followers") ?: 0
            UserScreen(
                userData = userData,
                onBack = {
                    navController.popBackStack()
                },
                igViewModel = igViewModel,
                onPostClick = {
                    navController.currentBackStackEntry?.savedStateHandle?.set("postData", it)
                    navController.navigate(Screens.SinglePostScreen.route)
                },
                followers = followers
            )
        }

        // profile screen
        composable(Screens.ProfileScreen.route) {
            ProfileScreen(
                igViewModel = igViewModel,
                onBack = {
                    navController.popBackStack()
                },
                onLogout = {
                    navController.navigate(Screens.LoginInScreen.route) {
                        popUpTo(0)
                    }
                }
            )
        }

        // new post screen
        composable(Screens.NewPostScreen.route) {   navBackStackEntry ->
            val imageUri = navBackStackEntry.arguments?.getString("imageUri")
            imageUri?.let {
                NewPostScreen(
                    igViewModel = igViewModel,
                    encodedUri = it,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }

        // about screen
        composable(Screens.AboutScreen.route) {
            AboutScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // login screen (added this just to handle the logout functionality)
        composable(
            route = Screens.LoginInScreen.route,
            enterTransition = {
                slideIntoContainer(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = EaseIn
                    ),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = EaseOut
                    ),
                    towards = AnimatedContentTransitionScope.SlideDirection.Left
                )
            }
        ) {
            LoginScreen(
                navigateToSignUpScreen = {
                    navController.navigate(Screens.SignUpScreen.route) {
                        popUpTo(Screens.LoginInScreen.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                modifier = modifier,
                igViewModel = igViewModel,
                goToFeedScreen = {
                    navController.navigate(Screens.FeedScreen.route) {
                        popUpTo(0)
                    }
                }
            )
        }
    }
}