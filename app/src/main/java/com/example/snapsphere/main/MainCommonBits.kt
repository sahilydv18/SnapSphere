package com.example.snapsphere.main

import android.widget.Toast
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.snapsphere.R
import com.example.snapsphere.Screens
import com.example.snapsphere.viewmodel.IgViewModel

// used to display toast on any error in the whole app
@Composable
fun NotificationToastMessage(
    igViewModel: IgViewModel
) {
    val notifyState = igViewModel.popupNotification.value

    val notifyMessage = notifyState?.handleContent()

    if (!notifyMessage.isNullOrBlank()) {
        Toast.makeText(LocalContext.current, notifyMessage, Toast.LENGTH_LONG).show()
    }
}

// used to redirect user to feed screen on successful signup or login
@Composable
fun CheckSignedIn(
    igViewModel: IgViewModel,
    goToFeedScreen: () -> Unit
) {
    val alreadyLoggedIn = remember {
        mutableStateOf(false)
    }

    val signedIn = igViewModel.signedIn.value

    if (signedIn && !alreadyLoggedIn.value) {
        alreadyLoggedIn.value = true
        goToFeedScreen()
    }
}

// bottom nav bar for the app
@Composable
fun BottomNavBar(
    currentScreen: Int,
    navigateToScreen: (Screens) -> Unit
) {

    var selectedItem by remember {
        mutableIntStateOf(currentScreen)
    }

    val items = listOf(
        Screens.FeedScreen,
        Screens.SearchScreen,
        Screens.MyPostsScreen
    )

    val selectedIcons = listOf(
        R.drawable.baseline_home_24,
        R.drawable.baseline_search_24,
        R.drawable.baseline_person_24
    )

    val unselectedIcons = listOf(
        R.drawable.outline_home_24,
        R.drawable.baseline_search_24,
        R.drawable.outline_person_24
    )

    NavigationBar {
        items.forEachIndexed { index, _ ->
            NavigationBarItem(
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    navigateToScreen(items[index])
                },
                icon = {
                    Icon(
                        painter = painterResource(id = if (selectedItem == index) selectedIcons[index] else unselectedIcons[index]),
                        contentDescription = null
                    )
                }
            )
        }
    }
}