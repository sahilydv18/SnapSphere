package com.example.snapsphere.main

import android.widget.Toast
import androidx.compose.runtime.Composable
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