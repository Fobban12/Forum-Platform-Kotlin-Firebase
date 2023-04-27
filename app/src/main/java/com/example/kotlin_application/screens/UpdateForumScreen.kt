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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.kotlin_application.navigation.Screens
import com.example.kotlin_application.utils.CreateNotification
import com.example.kotlin_application.viewmodel.ForumViewModel
import com.example.kotlin_application.viewmodel.UserProfileViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


//This is for the screen to make a forum post and push the data to the database.
@ExperimentalPermissionsApi
@ExperimentalComposeUiApi
@Composable
fun Updateforum(
    navController: NavController,
    forumId: String,
) {


//Top Bar
    Scaffold(
        topBar = {
            TopBar(
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
                    UpdateForumView(navController,forumId)
                }

            }

        }
    }






}

//The function that is used to post the forum
@ExperimentalPermissionsApi
@ExperimentalComposeUiApi
@Composable
fun UpdateForumView(
    navController: NavController,
    forumId: String,
) {

    //Get Forum ViewModel
    val viewModel: ForumViewModel = viewModel()
    //Get user profile view model
    val userProfileViewModel: UserProfileViewModel = viewModel();

    //Set effect to get single comment
    LaunchedEffect(forumId, viewModel) {
        viewModel.getSingleForum(forumId);
    }

    //Get state from user profile from view model
    val singleUser = userProfileViewModel.singleUserProfile;
    //Single forum data
    val singleForum = viewModel.singleForum;


    //For the Title Text field
    var title = remember(singleForum.value?.title) { mutableStateOf(singleForum.value?.title) }
    val titleIsValid = remember(title.value){title.value?.trim()?.isNotEmpty()}
    val titleLengthIsValid = remember(title.value){title.value?.trim()?.length?.let { it >=6 }}
    //For the description Text field
    var description = remember (singleForum.value?.description){ mutableStateOf(singleForum.value?.description) }
    val descriptionIsValid = remember(description.value){description.value?.trim()?.isNotEmpty()}
    val descriptionLengthIsValid = remember(description.value){description.value?.trim()?.length?.let { it >=3 }}
    //Context and keyboard controller
    val keyboardController = LocalFocusManager.current;
    val context = LocalContext.current;

    //Get user UID
    val uid = FirebaseAuth.getInstance().uid.toString();


    //Check user is logged in or not
    val checkUserIsNull = remember(FirebaseAuth.getInstance().currentUser?.email) {
        FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()
    };

    //Set effect to fetch single user id

    LaunchedEffect(uid, checkUserIsNull, userProfileViewModel) {
        userProfileViewModel.fetchSingleUserProfile(uid);
    }

    //Image stuff
    var imageUri = remember { mutableStateOf<Uri?>(null) }
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
            imageUri.value = it
        }
    //Get storage reference
    val ref: StorageReference = FirebaseStorage.getInstance().reference




    //Title Text field

        OutlinedTextField(
        value = title?.value.toString(),
        onValueChange = { title.value = it },
        label = { Text(text = "Title", color = MaterialTheme.colors.onBackground) },
        enabled = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { keyboardController.clearFocus();
            Log.d("Title: ", title?.value.toString())
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
        value = description?.value.toString(),
        onValueChange = { description?.value = it },
        label = { Text(text = "Description", color = MaterialTheme.colors.onBackground) },
        enabled = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { keyboardController.clearFocus()

            Log.d("Description: ", description?.value.toString())
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
        //Get the current Forum image
        val firstImage = rememberAsyncImagePainter(
            ImageRequest.Builder(LocalContext.current).data(data = "${singleForum.value?.image}")
                .apply(block = fun ImageRequest.Builder.() {

                }).build()
        )

        Spacer(modifier = Modifier.height(12.dp))
        Text("Current image of the Forum")
        Spacer(modifier = Modifier.height(12.dp))
        Image(
            painter = firstImage,
            contentDescription = "Old Image from forum",
            modifier = Modifier.size(150.dp)
        )



        Spacer(modifier = Modifier.height(12.dp))


        imageUri.value.let {
            if (Build.VERSION.SDK_INT < 28) {
                bitmap.value = MediaStore.Images
                    .Media.getBitmap(context.contentResolver, it)
            } else {
                val source = it?.let { it1 ->
                    ImageDecoder.createSource(context.contentResolver,
                        it1
                    )
                }
                bitmap.value = source?.let { it1 -> ImageDecoder.decodeBitmap(it1) }
            }
            Text("New image of the Forum")
            Spacer(modifier = Modifier.height(12.dp))
            bitmap.value?.let { btm ->
                Image(
                    bitmap = btm.asImageBitmap(),
                    contentDescription = "Added image",
                    modifier = Modifier.size(150.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))


        Button(onClick = {


            launcher.launch("image/*")


        }) {
            Text(text = "Pick an Image")
        }


    }
    Spacer(modifier = Modifier.height(12.dp))
    Button(onClick = {

        if (!titleIsValid!! && !titleLengthIsValid!!) {
            Toast.makeText(context, "Forum Title invalid", Toast.LENGTH_LONG).show()
        }
        else if (!descriptionIsValid!! && !descriptionLengthIsValid!!){
            Toast.makeText(context, "Description is invalid", Toast.LENGTH_LONG).show()
        }
        else if (imageUri.value == null){
            viewModel.editForum(forumId,singleForum.value?.image.toString(), title.value.toString(),description.value.toString())
            navController.navigate(Screens.MainScreen.name);
        }
        else {
            imageUri?.value.let {
                if (it != null) {
                    ref.child("/users/${singleUser.value?.userId}/forum/${singleForum.value?.idImage}/image").putFile(it).addOnSuccessListener {
                        val urlDownload = ref.child("/users/${singleUser.value?.userId}/forum/${singleForum.value?.idImage}/image").downloadUrl
                        urlDownload.addOnSuccessListener {
                            viewModel.editForum(forumId,it.toString(), title.value.toString(),description.value.toString())
                            navController.navigate(Screens.MainScreen.name);
                            Log.d("Success", "Success!")
                            val createNotification = CreateNotification(context, "Edited forum Successfully", "Forum edited")
                            createNotification.showNotification()
                        }

                            .addOnFailureListener {
                                Toast.makeText(context, "Failed to edit forum", Toast.LENGTH_SHORT)
                                    .show() }


                    }
                }

            }


        }
        keyboardController.clearFocus()
    },  colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.onBackground) ) {
        Text(text = "Post", style = TextStyle(color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp), modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
    }
}

//For rendering the top bar.
@ExperimentalComposeUiApi
@Composable
fun TopBar(navController: NavController, IconClick: () -> Unit)
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
                Text(text = "Update forum")

            }
        },
        backgroundColor = MaterialTheme.colors.onBackground,
        contentColor = MaterialTheme.colors.onSecondary
    )

}