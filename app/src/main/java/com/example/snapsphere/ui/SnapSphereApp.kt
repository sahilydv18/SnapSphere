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
import com.example.snapsphere.ui.screens.FeedScreen
import com.example.snapsphere.ui.screens.MyPostsScreen
import com.example.snapsphere.ui.screens.NewPostScreen
import com.example.snapsphere.ui.screens.ProfileScreen
import com.example.snapsphere.ui.screens.SearchScreen
import com.example.snapsphere.ui.screens.SinglePostScreen
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
                modifier = modifier,
                navigateToScreen = { screen: Screens ->
                    navController.navigate(screen.route) {
                        popUpTo(0)
                    }
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
                modifier = modifier,
                navigateToScreen = { screen: Screens ->
                    navController.navigate(screen.route) {
                        popUpTo(0)
                    }
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
                }
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