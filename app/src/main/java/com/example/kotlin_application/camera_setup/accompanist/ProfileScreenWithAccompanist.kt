package com.example.kotlin_application.camera_setup.accompanist
import android.graphics.Bitmap
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState


@ExperimentalPermissionsApi
@Composable
fun ProfileScreenWithAccompanist() {
    var resultBitmap: Bitmap? by rememberSaveable { mutableStateOf(placeHolderBitmap) }

    val launcherForImageCapture = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) {
        resultBitmap = if (it.toString().isEmpty()) {
            placeHolderBitmap
        } else {
            it
        }
    }

    ScreenContentAccompanist(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        resultBitmap = resultBitmap,
        launcherForImageCapture = launcherForImageCapture
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun ScreenContentAccompanist(
    modifier: Modifier,
    cameraPermission: PermissionState = cameraPermissionState(),
    resultBitmap: Bitmap?,
    launcherForImageCapture: ManagedActivityResultLauncher<Void?, Bitmap?>
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
            )
        )
        Spacer(modifier = Modifier.height(24.dp))
        Divider(
            modifier = Modifier.fillMaxWidth(),
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(100.dp)
                .width(100.dp)
                .clip(shape = CircleShape)
                .border(
                    width = 1.dp,
                    color = Color.Gray,
                    shape = CircleShape
                )
        ) {
            resultBitmap?.asImageBitmap()?.let {
                Image(
                    bitmap = it,
                    contentDescription = "Captured image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(150.dp)
                        .clickable {
                            if (cameraPermission.permission != null) {
                                launcherForImageCapture.launch()
                            } else {
                                cameraPermission.launchPermissionRequest()
                            }
                        }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

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

@Composable
fun ProfileOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit = { }
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.padding(vertical = 10.dp),
        textStyle = TextStyle(color = Color.Gray)
    )
}