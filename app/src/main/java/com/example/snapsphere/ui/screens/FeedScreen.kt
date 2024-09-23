package com.example.snapsphere.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.snapsphere.R
import com.example.snapsphere.Screens
import com.example.snapsphere.data.PostData
import com.example.snapsphere.data.UserData
import com.example.snapsphere.utils.BottomNavBar
import com.example.snapsphere.utils.CommonImage
import com.example.snapsphere.utils.CommonProgressSpinner
import com.example.snapsphere.utils.UserImage
import com.example.snapsphere.viewmodel.IgViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    igViewModel: IgViewModel,
    navigateToScreen: (Screens) -> Unit,
    navigateToUserProfile: (UserData) -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentScreen = 0,
                navigateToScreen = { screen: Screens ->
                    navigateToScreen(screen)
                }
            )
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        fontSize = MaterialTheme.typography.displaySmall.fontSize,
                        fontFamily = FontFamily.Cursive,
                        style = TextStyle(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFFF6F61),
                                    Color(0xFF9B59B6),
                                    Color(0xFF008080)
                                )
                            )
                        ),
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { innerPadding ->
        Box {
            if (igViewModel.inProgress.value) {
                CommonProgressSpinner()
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (igViewModel.postFeedProgress.value) {
                    CommonProgressSpinner()
                } else {
                    LazyColumn {
                        items(igViewModel.postFeed.value) {
                            FeedPost(
                                postData = it,
                                igViewModel = igViewModel,
                                navigateToUserProfile = navigateToUserProfile,
                                navigateToScreen = navigateToScreen
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FeedPost(
    postData: PostData,
    igViewModel: IgViewModel,
    navigateToUserProfile: (UserData) -> Unit,
    navigateToScreen: (Screens) -> Unit
) {
    Column(
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        // username and image
        Row(
            modifier = Modifier
                .padding(bottom = 8.dp, start = 8.dp)
                .clickable {
                    igViewModel.getAnotherUserData(postData.userId!!) { userData: UserData ->
                        if (userData.userId != igViewModel.userData.value?.userId) {
                            navigateToUserProfile(userData)
                            igViewModel.getSearchedUserPost(userId = postData.userId)
                        } else {
                            navigateToScreen(Screens.MyPostsScreen)
                        }
                    }
                }
        ) {
            UserImage(image = postData.userImage, modifier = Modifier.size(40.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = postData.username ?: "",
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }

        // post image
        CommonImage(
            image = postData.postImage,
            modifier = Modifier.fillMaxWidth()
        )

        // like and comment button
        Column {
            var noOfLikes by remember {
                mutableIntStateOf(postData.likes?.size ?: 0)
            }

            var showLiked by remember {
                mutableStateOf(postData.likes?.contains(igViewModel.userData.value?.userId) == true)
            }

            Row {
                IconButton(
                    onClick = {
                        showLiked = !showLiked
                        igViewModel.onPostLike(postData) {  isLiked ->
                            if (isLiked) {
                                noOfLikes++
                            } else {
                                if (noOfLikes > 0) {
                                    noOfLikes--
                                }
                            }
                        }
                    },
                    modifier = Modifier.padding(start = 0.dp)
                ) {
                    Icon(
                        imageVector = if (showLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint = if (showLiked) Color.Red else MaterialTheme.colorScheme.onSurface
                    )
                }

                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_mode_comment_24),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Text(
                text = "$noOfLikes " + stringResource(id = R.string.likes),
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        // post description
        if (!postData.postDescription.isNullOrBlank()) {
            Row(
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(
                    text = postData.username ?: "",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = postData.postDescription)
            }
        }
    }
}