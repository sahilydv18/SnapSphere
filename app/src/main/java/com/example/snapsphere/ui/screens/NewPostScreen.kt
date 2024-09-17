package com.example.snapsphere.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.snapsphere.R
import com.example.snapsphere.utils.CommonProgressSpinner
import com.example.snapsphere.viewmodel.IgViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewPostScreen(
    igViewModel: IgViewModel,
    encodedUri: String,
    onBack: () -> Unit
) {
    val image by remember {
        mutableStateOf(encodedUri)
    }

    var description by rememberSaveable {
        mutableStateOf("")
    }

    val focus = LocalFocusManager.current

    if (igViewModel.inProgress.value) {
        CommonProgressSpinner()
    } else {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = stringResource(id = R.string.new_post),
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            TextButton(onClick = {
                                focus.clearFocus(force = true)
                            }) {
                                Text(text = stringResource(id = R.string.post))
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            onBack()
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // image for post
                Image(
                    painter = rememberAsyncImagePainter(image),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 80.dp),
                    contentScale = ContentScale.FillWidth
                )

                Spacer(modifier = Modifier.height(16.dp))

                // description text area
                OutlinedTextField(
                    value = description,
                    onValueChange = {
                        description = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    singleLine = false,
                    shape = RoundedCornerShape(10.dp),
                    label = {
                        Text(text = stringResource(id = R.string.description))
                    },
                    placeholder = {
                        Text(text = stringResource(id = R.string.description_placeholder))
                    }
                )
            }
        }
    }
}