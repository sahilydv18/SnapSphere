package com.example.snapsphere.ui.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.snapsphere.R
import com.example.snapsphere.Screens
import com.example.snapsphere.main.BottomNavBar
import com.example.snapsphere.viewmodel.IgViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPostsScreen(
    igViewModel: IgViewModel,
    navigateToScreen: (Screens) -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentScreen = 2,
                navigateToScreen = { screen: Screens ->
                    navigateToScreen(screen)
                }
            )
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "@${igViewModel.userData.value?.username ?: ""}",
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            )
        }
    ) { innerPadding ->

        Box {
            if(igViewModel.inProgress.value) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
            ) {
                // image and basic account info
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ProfileImage(
                        image = igViewModel.userData.value?.imageUrl,
                        onClick = {},
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    BasicAccountInfo(
                        about = R.string.posts,
                        value = 5,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    BasicAccountInfo(
                        about = R.string.followers,
                        value = 5,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    BasicAccountInfo(
                        about = R.string.following,
                        value = 5,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                
                // name and bio
                Text(
                    text = igViewModel.userData.value?.name ?: "",
                    fontWeight = FontWeight.Bold
                )
                Text(text = igViewModel.userData.value?.bio ?: "")

                Spacer(modifier = Modifier.height(8.dp))

                // edit profile button
                OutlinedButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10)
                ) {
                    Text(text = stringResource(id = R.string.edit_profile))
                }

                Spacer(modifier = Modifier.height(8.dp))

                // posts
                Column(
                    modifier = Modifier.weight(1f).fillMaxSize()
                        .background(Color.Gray),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Posts List")
                }
            }
        }
    }
}

@Composable
fun ProfileImage(
    image: String?,
    onClick: () -> Unit,
    modifier: Modifier
) {
    Image(
        painter = painterResource(id = R.drawable.guy),
        contentDescription = null,
        modifier = modifier
            .size(56.dp)
            .clip(CircleShape)
    )
}

@Composable
fun BasicAccountInfo(
    @StringRes about: Int,
    value: Int,
    modifier: Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 4.dp)
    ) {
        Text(
            text = value.toString(),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(text = stringResource(id = about))
    }
}