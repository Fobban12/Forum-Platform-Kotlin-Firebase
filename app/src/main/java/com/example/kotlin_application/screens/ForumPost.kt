package com.example.kotlin_application.screens

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kotlin_application.navigation.Screens
import com.example.kotlin_application.screens.authentication.UserForm
import com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Label


//This is for the screen to make a forum post and push the data to the database.
@ExperimentalComposeUiApi
@Composable
fun ForumPost(
    navController: NavController,
) {

//Top Bar
    Scaffold(
        topBar = {
            renderTopBar(
                navController,
                IconClick = {
                    navController.navigate(Screens.MainScreen.name)
                })
        }
    )
    {it
    Surface(modifier = Modifier
        .fillMaxSize())
      {
        Box()
        {

            Column(modifier = Modifier
                .padding(10.dp))

            {
                Text(text = "Topic Title:", modifier = Modifier.padding(bottom = 10.dp))
                TestField()
                TestImage()
            }

        }

      }
    }






}

//For rendering the top bar.
@ExperimentalComposeUiApi
@Composable
fun renderTopBar(navController: NavController, IconClick: () -> Unit)
{

    TopAppBar(
        navigationIcon = {
            IconButton(onClick = IconClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Go Back"
                )
            }
        },
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                Text(text = "Create Forum")

            }
        },
        backgroundColor = MaterialTheme.colors.onBackground,
        contentColor = MaterialTheme.colors.onSecondary
    )

}//Here would be the text field for the title
@Composable
fun TestField() {
    var text by remember { mutableStateOf("") }

    TextField(
        value = text,
        onValueChange = { text = it },
        label = { Text("Title") }
    )
}

@Composable
fun TestImage()
{
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
        }

    Column(

    ) {

        imageUri?.let {
            if (Build.VERSION.SDK_INT < 28) {
                bitmap.value = MediaStore.Images
                    .Media.getBitmap(context.contentResolver, it)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, it)
                bitmap.value = ImageDecoder.decodeBitmap(source)
            }

            bitmap.value?.let { btm ->
                Image(
                    bitmap = btm.asImageBitmap(),
                    contentDescription = "Added image",

                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = { launcher.launch("image/*") }) {
            Text(text = "Pick Image")
        }
    }

}