package com.example.snapsphere.ui.screens

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.snapsphere.BuildConfig
import com.example.snapsphere.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onBack: () -> Unit
) {

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {}
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.about))
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
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // app name and image
            Row(
                modifier = Modifier.padding(top = 128.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.snapsphere_icon),
                    contentDescription = "app_icon",
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Text(
                    text = stringResource(id = R.string.app_name),
                    modifier = Modifier.padding(16.dp),
                    fontSize = MaterialTheme.typography.displayMedium.fontSize,
                    fontFamily = FontFamily.Cursive,
                    style = TextStyle(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFFFF6F61), Color(0xFF9B59B6), Color(0xFF008080))
                        )
                    ),
                    fontWeight = FontWeight.Bold
                )
            }

            // version name
            Text(
                text = stringResource(id = R.string.version) + BuildConfig.VERSION_NAME,
                fontSize = MaterialTheme.typography.titleSmall.fontSize
            )

            Spacer(modifier = Modifier.height(64.dp))

            // made by text
            Text(
                text = stringResource(id = R.string.made_by_text),
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // links to connect on other platform
            Row {
                // email
                IconButton(onClick = {
                    try {
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "vnd.android.cursor.item/email"
                            putExtra(Intent.EXTRA_EMAIL, arrayOf("ydvvsahil09@gmail.com"))
                        }
                        launcher.launch(intent)
                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(context, "No email app found", Toast.LENGTH_LONG).show()
                    } catch (t: Throwable) {
                        Toast.makeText(context, "An error occurred", Toast.LENGTH_LONG).show()
                    }
                }) {
                    Image(
                        imageVector = Icons.Default.Email,
                        modifier = Modifier.size(28.dp),
                        contentDescription = "email",
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                    )
                }

                // github
                IconButton(onClick = {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse("https://github.com/sahilydv18")
                    }
                    launcher.launch(intent)
                }) {
                    Image(
                        painter = painterResource(id = R.drawable.github_24),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                    )
                }

                // linkedIn
                IconButton(onClick = {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse("https://www.linkedin.com/in/ydvsahil18")
                    }
                    launcher.launch(intent)
                }) {
                    Image(
                        painter = painterResource(id = R.drawable.linkedin_24),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                    )
                }

                // twitter (x)
                IconButton(onClick = {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse("https://x.com/sahil_yadvv")
                    }
                    launcher.launch(intent)
                }) {
                    Image(
                        painter = painterResource(id = R.drawable.twitter_alt_24),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                    )
                }
            }
        }
    }
}