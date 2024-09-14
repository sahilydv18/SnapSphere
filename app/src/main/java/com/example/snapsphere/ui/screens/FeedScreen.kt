package com.example.snapsphere.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.snapsphere.Screens
import com.example.snapsphere.main.BottomNavBar
import com.example.snapsphere.viewmodel.IgViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FeedScreen(
    igViewModel: IgViewModel,
    modifier: Modifier,
    navigateToScreen: (Screens) -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentScreen = 0,
                navigateToScreen = { screen: Screens ->
                    navigateToScreen(screen)
                }
            )
        }
    ) {
        Box {
            if(igViewModel.inProgress.value) {
                Column(
                    modifier = Modifier.fillMaxSize()
                        .background(Color.Gray),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Feed Screen")
            }
        }
    }
}