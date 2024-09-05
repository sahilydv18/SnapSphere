package com.example.snapsphere

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
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

            SnapSphereTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SnapSphereApp(
                        modifier = Modifier.padding(innerPadding),
                        igViewModel = igViewModel
                    )
                }
            }
        }
    }
}