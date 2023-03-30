package com.example.kotlin_application.camera_setup.permissionLauncher

import android.content.Context
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlin_application.permissions.CAMERA_PERMISSION
import com.example.kotlin_application.permissions.isPermissionGranted
import com.example.kotlin_application.permissions.samplePermissions
import com.example.kotlin_application.camera_setup.accompanist.placeHolderBitmap
import com.example.kotlin_application.camera_setup.accompanist.ProfileOutlinedTextField

@Composable
fun ProfileScreen() {

    val launcherForImageCapture = permissionLauncher()

    ScreenContent(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        launcherForImageCapture = launcherForImageCapture
    )
}

@Composable
private fun ScreenContent(
    modifier: Modifier,
    launcherForImageCapture: ManagedActivityResultLauncher<String, Boolean>,
    context: Context = LocalContext.current
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(20.dp)
    ) {
        Text(
            text = "Create your profile here!",
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            ),
            modifier = Modifier.padding(10.dp)
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .height(100.dp)
                .width(100.dp)
                .clip(shape = CircleShape)
                .border(
                    width = 1.dp,
                    color = Color.Gray,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                bitmap = placeHolderBitmap.asImageBitmap(),
                contentDescription = "Captured image",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .size(150.dp)
                    .clickable {
                        // Check if single permission
                        if (isPermissionGranted(context, CAMERA_PERMISSION)) {
                            // Permission granted, do your thing
                        } else {
                            // Permissions denied.
                            // Show a contextual dialog, ask to accept permissions
                            launcherForImageCapture.launch(CAMERA_PERMISSION)
                        }

                        // Check if list of permissions
                        if (isPermissionGranted(context, listOf(CAMERA_PERMISSION))) {
                            // Permission granted, do your thing
                        } else {
                            // Permissions denied.
                            // Show a contextual dialog, ask to accept permissions
                            launcherForImageCapture.launch(
                                samplePermissions()
                                    .toTypedArray()
                                    .toString()
                            )
                        }
                    }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        ProfileOutlinedTextField(value = "Name")
        ProfileOutlinedTextField(value = "Email")
        ProfileOutlinedTextField(value = "Password")
        ProfileOutlinedTextField(value = "DOB")

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { },
            modifier = Modifier
                .padding(horizontal = 36.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Submit")
        }
    }
}