package com.example.snapsphere

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.snapsphere.utils.NotificationToastMessage
import com.example.snapsphere.ui.LoginAndSignupNav
import com.example.snapsphere.ui.SnapSphereApp
import com.example.snapsphere.ui.theme.SnapSphereTheme
import com.example.snapsphere.viewmodel.IgViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val igViewModel: IgViewModel = hiltViewModel()

            var signedIn by remember {
                mutableStateOf<Boolean?>(null)
            }

            LaunchedEffect(igViewModel.signedIn.value) {
                signedIn = igViewModel.signedIn.value
            }

            NotificationToastMessage(igViewModel = igViewModel)

            SnapSphereTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (signedIn == false) {
                        // this nav host will be used when the user is not signed in
                        LoginAndSignupNav(
                            modifier = Modifier.padding(innerPadding),
                            igViewModel = igViewModel
                        )
                    } else if (signedIn == true) {
                        // this nav host will be used when the user is signed in
                        SnapSphereApp(
                            igViewModel = igViewModel,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}