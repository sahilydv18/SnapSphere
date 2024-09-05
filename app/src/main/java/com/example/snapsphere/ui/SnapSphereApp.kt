package com.example.snapsphere.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.snapsphere.Screens
import com.example.snapsphere.auth.SignUpScreen
import com.example.snapsphere.viewmodel.IgViewModel

@Composable
fun SnapSphereApp(
    modifier: Modifier,
    igViewModel: IgViewModel
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screens.SignUpScreen.route) {
        composable(Screens.SignUpScreen.route) {
            SignUpScreen(
                igViewModel = igViewModel,
                modifier = modifier
            )
        }
    }
}

