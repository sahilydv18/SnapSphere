package com.example.snapsphere.ui.screens

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.snapsphere.R
import com.example.snapsphere.data.PostData
import com.example.snapsphere.utils.CommonImage
import com.example.snapsphere.utils.CommonProgressSpinner
import com.example.snapsphere.utils.UserImage
import com.example.snapsphere.viewmodel.IgViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SinglePostScreen(
    igViewModel: IgViewModel,
    postData: PostData,
    onBack: () -> Unit
) {
    // whenever on the single post screen we will get comments at the start
    postData.postId?.let {
        igViewModel.getComments(postId = it)
    }

    if (igViewModel.inProgress.value) {
        CommonProgressSpinner()
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { /*TODO*/ },
                    navigationIcon = {
                        IconButton(onClick = { onBack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            var commentText by rememberSaveable {
                mutableStateOf("")
            }
            val focusManager = LocalFocusManager.current


            Box {
                var isExpanded by rememberSaveable {
                    mutableStateOf(false)
                }

                if (isExpanded) {
                    ModalBottomSheet(
                        onDismissRequest = { isExpanded = false },
                        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                    ) {
                        LazyColumn(
                            modifier = Modifier.weight(1f)
                        ) {
                            if (igViewModel.commentsProgress.value) {
                                item {
                                    CommonProgressSpinner()
                                }
                            } else {
                                if (igViewModel.commentForPost.value.isEmpty()) {
                                    item {
                                        Text(
                                            text = stringResource(id = R.string.no_comment),
                                            modifier = Modifier.padding(16.dp)
                                        )
                                    }
                                } else {
                                    items(igViewModel.commentForPost.value) {
                                        CommentDisplay(commentData = it, igViewModel = igViewModel)
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = commentText,
                            onValueChange = { commentText = it },
                            shape = CircleShape,
                            placeholder = {
                                Text(text = stringResource(id = R.string.add_comment))
                            },
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        igViewModel.createComment(
                                            postId = postData.postId!!,
                                            text = commentText
                                        )
                                        focusManager.clearFocus(force = true)
                                        commentText = ""
                                    },
                                    enabled = commentText.isNotBlank()
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.Send,
                                        contentDescription = null
                                    )
                                }
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Send
                            ),
                            keyboardActions = KeyboardActions(
                                onSend = {
                                    if (commentText.isNotBlank()) {
                                        igViewModel.createComment(
                                            postId = postData.postId!!,
                                            text = commentText
                                        )
                                        focusManager.clearFocus(force = true)
                                        commentText = ""
                                    }
                                }
                            ),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp)
                ) {
                    // user image and username
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row {
                            UserImage(
                                image = postData.userImage,
                                modifier = Modifier.size(40.dp)
                            )
                            Text(
                                text = postData.username ?: "",
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .padding(start = 8.dp),
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        if (igViewModel.userData.value?.userId != postData.userId) {
                            TextButton(
                                onClick = {
                                    igViewModel.onFollowClick(postData.userId!!)
                                },
                                modifier = Modifier.align(Alignment.CenterVertically)
                            ) {
                                Text(
                                    text = stringResource(
                                        id = if (igViewModel.userData.value?.following?.contains(
                                                postData.userId
                                            ) == true
                                        ) R.string.unfollow else R.string.follow
                                    )
                                )
                            }
                        } else {
                            var menuExpanded by remember {
                                mutableStateOf(false)
                            }

                            Box {
                                IconButton(onClick = { menuExpanded = !menuExpanded }) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = null
                                    )
                                }
                                DropdownMenu(
                                    expanded = menuExpanded,
                                    onDismissRequest = { menuExpanded = false }) {
                                    DropdownMenuItem(
                                        text = { Text(text = stringResource(id = R.string.delete)) },
                                        onClick = {
                                            igViewModel.onDeletePost(
                                                postId = postData.postId,
                                                imageUrl = postData.postImage,
                                                onPostDeleteSuccess = {
                                                    onBack()
                                                }
                                            )
                                        })
                                }
                            }
                        }
                    }

                    if (igViewModel.userData.value?.userId == postData.userId) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // post image
                    CommonImage(image = postData.postImage)

                    // like button and post likes
                    Row {
                        var showLiked by remember {
                            mutableStateOf(postData.likes?.contains(igViewModel.userData.value?.userId) == true)
                        }
                        var noOfLikes by remember {
                            mutableIntStateOf(postData.likes?.size ?: 0)
                        }

                        IconButton(
                            onClick = {
                                showLiked = !showLiked
                                igViewModel.onPostLike(postData) { isLiked ->
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

                        Text(
                            text = "$noOfLikes " + stringResource(id = R.string.likes),
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )

                        IconButton(
                            onClick = {
                                //igViewModel.getComments(postId = postData.postId!!)
                                isExpanded = true
                            }
                        ) {
                            Icon(painter = painterResource(id = R.drawable.outline_comment_24), contentDescription = null)
                        }
                    }

                    // post description
                    if (!postData.postDescription.isNullOrBlank()) {
                        Row {
                            Text(
                                text = postData.username ?: "",
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = postData.postDescription)
                        }
                    }

                    // comments
                    Text(
                        text = "${igViewModel.commentForPost.value.size} " + stringResource(id = R.string.comments),
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .clickable {
                                isExpanded = true
                            },
                        color = Color.Gray
                    )
                }
            }
        }
    }
}