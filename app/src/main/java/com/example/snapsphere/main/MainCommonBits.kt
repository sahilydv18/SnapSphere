package com.example.snapsphere.main

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.snapsphere.viewmodel.IgViewModel

@Composable
fun NotificationToastMessage(
    igViewModel: IgViewModel
) {
    val notifyState = igViewModel.popupNotification.value

    val notifyMessage = notifyState?.handleContent()

    if(!notifyMessage.isNullOrBlank()) {
        Toast.makeText(LocalContext.current, notifyMessage, Toast.LENGTH_LONG).show()
    }
}

@Composable
fun CheckSignedIn(
    igViewModel: IgViewModel,
    goToFeedScreen: () -> Unit
) {
    val alreadyLoggedIn = remember {
        mutableStateOf(false)
    }

    val signedIn = igViewModel.signedIn.value

    if(signedIn == true && !alreadyLoggedIn.value) {
        alreadyLoggedIn.value = true
        goToFeedScreen()
    }
}