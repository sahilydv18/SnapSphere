package com.example.snapsphere.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.snapsphere.R
import com.example.snapsphere.utils.CommonProgressSpinner
import com.example.snapsphere.utils.UserImage
import com.example.snapsphere.viewmodel.IgViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    igViewModel: IgViewModel,
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
    var name by rememberSaveable {
        mutableStateOf(igViewModel.userData.value?.name ?: "")
    }

    var bio by rememberSaveable {
        mutableStateOf(igViewModel.userData.value?.bio ?: "")
    }

    var username by rememberSaveable {
        mutableStateOf(igViewModel.userData.value?.username ?: "")
    }

    val focus = LocalFocusManager.current

    // launcher for getting image from the user's gallery
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                igViewModel.uploadProfileImage(it)
            }
        }

    if (igViewModel.inProgress.value) {
        CommonProgressSpinner()
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = stringResource(id = R.string.edit_profile),
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            TextButton(onClick = {
                                focus.clearFocus(force = true)
                                igViewModel.updateUserProfile(
                                    name = name,
                                    username = username,
                                    bio = bio
                                )
                            }) {
                                Text(text = stringResource(id = R.string.save))
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { onBack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null
                            )
                        }
                    }
                )
            },
            bottomBar = {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(bottom = 40.dp)
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(
                        onClick = {
                            igViewModel.onLogout()
                            onLogout()
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color.Red
                        )
                    ) {
                        Text(text = stringResource(id = R.string.logout))
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp)
                        .fillMaxSize()
                ) {
                    // user image
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                            .clickable {
                                launcher.launch("image/*")
                            },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        UserImage(
                            image = igViewModel.userData.value?.imageUrl,
                            modifier = Modifier.size(120.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stringResource(id = R.string.change_pfp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // name text field
                    Column {
                        Text(
                            text = stringResource(id = R.string.name),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = name,
                            onValueChange = {
                                name = it
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(100),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // username text field
                    Column {
                        Text(
                            text = stringResource(id = R.string.username),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = username,
                            onValueChange = {
                                username = it
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(100),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done
                            ),
                            prefix = {
                                Text(text = stringResource(id = R.string.at))
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // bio text area
                    Column {
                        Text(
                            text = stringResource(id = R.string.bio),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = bio,
                            onValueChange = {
                                bio = it
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp),
                            singleLine = false,
                            shape = RoundedCornerShape(10),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done
                            )
                        )
                    }
                }
            }
        }
    }
}