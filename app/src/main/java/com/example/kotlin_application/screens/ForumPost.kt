package com.example.kotlin_application.screens

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.size.Size
import com.example.kotlin_application.data.Forum
import com.example.kotlin_application.navigation.Screens
import com.example.kotlin_application.viewmodel.ForumViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*


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
                PostingForum(navController)
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











}//The function that is used to post the forum
@Composable
fun PostingForum(
    navController: NavController,
    viewModel: ForumViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {

   //For the Title Text field
    var title by remember { mutableStateOf("") }
    val titleIsValid = remember(title){title.trim().isNotEmpty()}
    val titleLengthIsValid = remember(title){title.trim().length >= 6}

    //For the description Text field
    var description by remember { mutableStateOf("") }
    val descriptionIsValid = remember(title){title.trim().isNotEmpty()}
    val descriptionLengthIsValid = remember(title){title.trim().length >= 3}


    val keyboardController = LocalFocusManager.current;
    val context = LocalContext.current;

    val username = FirebaseAuth.getInstance().currentUser?.email?.split("@")?.get(0)
    val uid = FirebaseAuth.getInstance().uid.toString();

    //Image stuff
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
        }
    //Get storage reference
    val ref: StorageReference = FirebaseStorage.getInstance().reference



    //Title Text field
    OutlinedTextField(
        value = title,
        onValueChange = { title = it },
        label = { Text(text = "Title", color = MaterialTheme.colors.onBackground) },
        enabled = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { keyboardController.clearFocus();
            Log.d("Title: ", title)
        }), singleLine = false, modifier = Modifier.fillMaxWidth(), textStyle = TextStyle(
            fontSize = 18.sp,
            color = MaterialTheme.colors.onBackground
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colors.onBackground,
            unfocusedBorderColor = Color.Gray,
            disabledBorderColor = Color.LightGray
        )

    )

    Spacer(modifier = Modifier.height(10.dp))

    //Description Text field
    OutlinedTextField(
        value = description,
        onValueChange = { description = it },
        label = { Text(text = "Description", color = MaterialTheme.colors.onBackground) },
        enabled = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { keyboardController.clearFocus()

            Log.d("Description: ", description)
        }), singleLine = false, modifier = Modifier.fillMaxWidth(), textStyle = TextStyle(
            fontSize = 18.sp,
            color = MaterialTheme.colors.onBackground
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colors.onBackground,
            unfocusedBorderColor = Color.Gray,
            disabledBorderColor = Color.LightGray
        )

    )

    //Image picker
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
                    modifier = Modifier.size(200.dp)
                    )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = { launcher.launch("image/*");
        }) {
            Text(text = "Pick Image")
        }


    }
    Spacer(modifier = Modifier.height(12.dp))
    Button(onClick = {

        if (!titleIsValid || !titleLengthIsValid ) {
            Toast.makeText(context, "Forum Title invalid", Toast.LENGTH_LONG).show()
        }
        else if (!descriptionIsValid || !descriptionLengthIsValid){
            Toast.makeText(context, "Description is invalid", Toast.LENGTH_LONG).show()
       }
        else {
             imageUri?.let {
                 var randomUID = UUID.randomUUID()
                 ref.child("/users/$username/forum/$randomUID/image").putFile(it).addOnSuccessListener {
                     val urlDownload = ref.child("/users/$username/forum/$randomUID/image").downloadUrl
                     urlDownload.addOnSuccessListener {
                         val newForumPost = Forum(title = title.trim(), description = description.trim(), id = randomUID.toString(), type = "Marketplace", image = it.toString(), createdAt = Timestamp.now(), userId = uid, username = username)
                         viewModel.createForum(newForumPost, context);
                         navController.navigate(Screens.MainScreen.name);
                         Log.d("Success", "Success!") }
                    .addOnFailureListener {
                        Toast.makeText(context, "Fail to post forum", Toast.LENGTH_SHORT)
                            .show() }


                 }

            }


        }
        keyboardController.clearFocus()
    },  colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.onBackground) ) {
        Text(text = "Post", style = TextStyle(color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp), modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
    }
}


