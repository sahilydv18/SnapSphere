package com.example.snapsphere.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.snapsphere.Screens
import com.example.snapsphere.main.BottomNavBar
import com.example.snapsphere.viewmodel.IgViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchScreen(
    igViewModel: IgViewModel,
    modifier: Modifier,
    navigateToScreen: (Screens) -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentScreen = 1,
                navigateToScreen = { screen: Screens ->
                    navigateToScreen(screen)
                }
            )
        }
    ) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Search Screen")
        }
    }
}