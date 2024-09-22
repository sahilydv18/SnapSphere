package com.example.snapsphere.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.snapsphere.R
import com.example.snapsphere.Screens
import com.example.snapsphere.data.PostData
import com.example.snapsphere.utils.BottomNavBar
import com.example.snapsphere.utils.CommonImage
import com.example.snapsphere.utils.CommonProgressSpinner
import com.example.snapsphere.utils.UserImage
import com.example.snapsphere.viewmodel.IgViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPostsScreen(
    igViewModel: IgViewModel,
    navigateToScreen: (Screens) -> Unit,
    goToProfileScreen: () -> Unit,
    navigateToNewPostScreen: (String) -> Unit,
    onPostClick: (PostData) -> Unit
) {

    // launcher for selecting image when the user clicks on the pfp on the MyPostsScreen
    val createPostLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val encodedString = Uri.encode(it.toString())
                val route = Screens.NewPostScreen.createRoute(encodedString)
                navigateToNewPostScreen(route)
            }
        }

    if (igViewModel.inProgress.value) {
        CommonProgressSpinner()
    } else {
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
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp)
                    //.verticalScroll(rememberScrollState())
                ) {
                    // image and basic account info
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ProfileImage(
                            image = igViewModel.userData.value?.imageUrl,
                            onClick = {
                                createPostLauncher.launch("image/*")
                            },
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        BasicAccountInfo(
                            about = R.string.posts,
                            value = igViewModel.userPosts.value.size,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        BasicAccountInfo(
                            about = R.string.followers,
                            value = 5,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        BasicAccountInfo(
                            about = R.string.following,
                            value = igViewModel.userData.value?.following?.size ?: 0,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // name and bio
                    if (igViewModel.userData.value?.name != null) {
                        Text(
                            text = "${igViewModel.userData.value?.name}",
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Text(
                            text = stringResource(id = R.string.add_name),
                            modifier = Modifier.clickable {
                                goToProfileScreen()
                            },
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    if (igViewModel.userData.value?.bio != null) {
                        Text(text = "${igViewModel.userData.value?.bio}")
                    } else {
                        Text(
                            text = stringResource(id = R.string.add_bio),
                            modifier = Modifier.clickable {
                                goToProfileScreen()
                            },
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // edit profile button
                    OutlinedButton(
                        onClick = {
                            goToProfileScreen()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10)
                    ) {
                        Text(text = stringResource(id = R.string.edit_profile))
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // posts
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                    ) {
                        UserPosts(
                            isPostLoading = igViewModel.refreshPostsProgress.value,
                            posts = igViewModel.userPosts.value,
                            onPostClick = onPostClick
                        )
                    }
                }
            }
        }
    }
}

// composable for displaying posts
@Composable
fun UserPosts(
    isPostLoading: Boolean,
    posts: List<PostData>,
    onPostClick: (PostData) -> Unit
) {
    if (isPostLoading) {
        CommonProgressSpinner()
    } else {
        if (posts.isNotEmpty()) {
            LazyVerticalGrid(columns = GridCells.Fixed(3)) {
                items(posts) {
                    Post(
                        post = it,
                        onPostClick = onPostClick
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = stringResource(id = R.string.no_posts))
            }
        }
    }
}

@Composable
fun Post(
    post: PostData,
    onPostClick: (PostData) -> Unit
) {
    CommonImage(
        image = post.postImage,
        modifier = Modifier
            .size(width = 200.dp, height = 160.dp)
            .padding(end = 2.dp, bottom = 2.dp)
            .clickable {
                onPostClick(post)
            }
    )
}

// composable for displaying profile image for the user
@Composable
fun ProfileImage(
    image: String?,
    onClick: () -> Unit,
    modifier: Modifier
) {
    Box(
        modifier = modifier.clickable { onClick() }
    ) {
        UserImage(image = image)
        Image(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            modifier = Modifier
                .clip(CircleShape)
                .size(24.dp)
                .background(MaterialTheme.colorScheme.primary)
                .align(Alignment.BottomEnd),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
        )
    }
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