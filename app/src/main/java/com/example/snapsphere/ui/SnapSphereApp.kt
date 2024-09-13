package com.example.snapsphere.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.snapsphere.Screens
import com.example.snapsphere.auth.LoginScreen
import com.example.snapsphere.auth.SignUpScreen
import com.example.snapsphere.viewmodel.IgViewModel

@Composable
fun SnapSphereApp(
    modifier: Modifier,
    igViewModel: IgViewModel
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.LoginInScreen.route) {
        // login screen
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
                igViewModel = igViewModel
            )
        }

        // sign up screen
        composable(
            route = Screens.SignUpScreen.route,
            enterTransition = {
                slideIntoContainer(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = EaseIn
                    ),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = EaseOut
                    ),
                    towards = AnimatedContentTransitionScope.SlideDirection.Right
                )
            }
        ) {
            SignUpScreen(
                navigateToLoginScreen = {
                    navController.navigate(Screens.LoginInScreen.route) {
                        popUpTo(Screens.SignUpScreen.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                igViewModel = igViewModel,
                modifier = modifier
            )
        }
    }
}

