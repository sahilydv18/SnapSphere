package com.example.snapsphere.ui

import androidx.activity.compose.BackHandler
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
import com.example.snapsphere.ui.screens.FeedScreen
import com.example.snapsphere.ui.screens.MyPostsScreen
import com.example.snapsphere.ui.screens.ProfileScreen
import com.example.snapsphere.ui.screens.SearchScreen
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
                }
            )
        }

        // profile screen
        composable(Screens.ProfileScreen.route) {
            ProfileScreen(
                igViewModel = igViewModel,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}