package com.example.snapsphere.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.snapsphere.Screens
import com.example.snapsphere.ui.screen.FeedScreen
import com.example.snapsphere.viewmodel.IgViewModel

@Composable
fun SnapSphereApp(
    igViewModel: IgViewModel,
    modifier: Modifier
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screens.FeedScreen.route) {
        composable(Screens.FeedScreen.route) {
            FeedScreen(
                igViewModel = igViewModel,
                modifier = modifier
            )
        }
    }
}