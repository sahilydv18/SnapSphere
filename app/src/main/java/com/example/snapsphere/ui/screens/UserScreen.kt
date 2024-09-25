package com.example.snapsphere.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.snapsphere.R
import com.example.snapsphere.data.PostData
import com.example.snapsphere.data.UserData
import com.example.snapsphere.utils.UserImage
import com.example.snapsphere.viewmodel.IgViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(
    igViewModel: IgViewModel,
    userData: UserData,
    onBack: () -> Unit,
    onPostClick: (PostData) -> Unit,
    followers: Int
) {

    var followersValue by rememberSaveable {
        mutableIntStateOf(followers)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "@${userData.username ?: ""}",
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { innerPadding ->
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
                UserImage(
                    image = userData.imageUrl,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                BasicAccountInfo(
                    about = R.string.posts,
                    value = igViewModel.searchedUsersPost.value.size,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                BasicAccountInfo(
                    about = R.string.followers,
                    value = followersValue,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                BasicAccountInfo(
                    about = R.string.following,
                    value = userData.following?.size ?: 0,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // name and bio
            userData.name?.let {
                Text(
                    text = it,
                    fontWeight = FontWeight.Bold
                )
            }
            userData.bio?.let {
                Text(text = it)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // follow/unfollow button
            OutlinedButton(
                onClick = {
                    igViewModel.onFollowClick(userData.userId!!)
                    igViewModel.getAnotherUserData(userId = userData.userId!!) { _ , followers: Int ->
                        followersValue = followers
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10)
            ) {
                Text(
                    text = stringResource(id = if(igViewModel.userData.value?.following?.contains(userData.userId) == true) R.string.unfollow else R.string.follow)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            UserPosts(isPostLoading = igViewModel.searchedUserPostProgress.value, posts = igViewModel.searchedUsersPost.value) {
                onPostClick(it)
            }
        }
    }
}