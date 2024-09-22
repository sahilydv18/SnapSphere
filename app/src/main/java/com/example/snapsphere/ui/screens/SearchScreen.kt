package com.example.snapsphere.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.snapsphere.R
import com.example.snapsphere.Screens
import com.example.snapsphere.data.PostData
import com.example.snapsphere.data.UserData
import com.example.snapsphere.utils.BottomNavBar
import com.example.snapsphere.utils.CommonProgressSpinner
import com.example.snapsphere.utils.UserImage
import com.example.snapsphere.viewmodel.IgViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchScreen(
    igViewModel: IgViewModel,
    modifier: Modifier,
    navigateToScreen: (Screens) -> Unit,
    onPostClick: (PostData) -> Unit
) {
    var searchTerm by rememberSaveable {
        mutableStateOf("")
    }

    var showSearchText by rememberSaveable {
        mutableStateOf(true)
    }

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
        if (igViewModel.searchedPostsProgress.value) {
            CommonProgressSpinner()
        } else {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // search bar
                OutlinedTextField(
                    value = searchTerm,
                    onValueChange = { searchTerm = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    shape = CircleShape,
                    placeholder = {
                        Text(text = stringResource(id = R.string.search))
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                igViewModel.searchPosts(searchTerm)
                                igViewModel.searchUsers(searchTerm)
                                showSearchText = false
                            },
                            enabled = searchTerm.isNotBlank()
                        ) {
                            Icon(imageVector = Icons.Default.Search, contentDescription = null)
                        }
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            igViewModel.searchPosts(searchTerm)
                            igViewModel.searchUsers(searchTerm)
                            showSearchText = false
                        }
                    ),
                    singleLine = true,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(16.dp))

                // displaying searched users and posts
                if (showSearchText) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "Search here...")
                    }
                } else {
                    // tabs
                    var state by rememberSaveable {
                        mutableIntStateOf(0)
                    }

                    val titles = listOf(R.string.users, R.string.posts_search)

                    SecondaryTabRow(selectedTabIndex = state) {
                        titles.forEachIndexed { index, title ->
                            Tab(selected = state == index, onClick = { state = index }) {
                                Text(
                                    text = stringResource(id = title),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    if (state == 0) {
                        SearchedUsers(
                            isUsersLoading = igViewModel.searchedUserProgress.value,
                            users = igViewModel.searchedUsers.value
                        ) {

                        }
                    } else {
                        SearchedPosts(
                            isPostLoading = igViewModel.searchedPostsProgress.value,
                            posts = igViewModel.searchedPosts.value
                        ) { postData: PostData ->
                            onPostClick(postData)
                        }
                    }
                }
            }
        }
    }
}

// composable for displaying searched users
@Composable
fun SearchedUsers(
    isUsersLoading: Boolean,
    users: List<UserData>,
    onUserClick: (UserData) -> Unit
) {
    if (isUsersLoading) {
        CommonProgressSpinner()
    } else {
        if (users.isNotEmpty()) {
            LazyColumn {
                items(users) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onUserClick(it)
                            }
                            .padding(vertical = 8.dp)
                    ) {
                        UserImage(
                            image = it.imageUrl,
                            modifier = Modifier.size(60.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(
                            modifier = Modifier.align(Alignment.CenterVertically)
                        ) {
                            Text(
                                text = it.username ?: "",
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = it.name ?: "",
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = stringResource(id = R.string.no_users))
            }
        }
    }
}

// composable for displaying searched posts
@Composable
fun SearchedPosts(
    isPostLoading: Boolean,
    posts: List<PostData>,
    onPostClick: (PostData) -> Unit
) {
    if (isPostLoading) {
        CommonProgressSpinner()
    } else {
        if (posts.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
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
                Text(text = stringResource(id = R.string.no_posts_search))
            }
        }
    }
}