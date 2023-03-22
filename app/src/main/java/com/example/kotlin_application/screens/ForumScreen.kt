package com.example.kotlin_application.screens

<<<<<<< HEAD
import android.icu.text.CaseMap.Title
import android.util.Log
import androidx.activity.ComponentDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
=======
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
>>>>>>> a284ecde409075fbeddce9d4ef116924167f605b
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kotlin_application.data.BottomNavItem
import com.example.kotlin_application.data.MenuItem
import com.example.kotlin_application.navigation.BottomNavigationBar
import com.example.kotlin_application.navigation.Drawer
import com.example.kotlin_application.navigation.DrawerBody
import com.example.kotlin_application.navigation.Screens
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
<<<<<<< HEAD
import java.time.format.TextStyle

@ExperimentalComposeUiApi
@Composable
fun ForumScreen(navController: NavController){
    val scaffoldState = rememberScaffoldState();
    val showDialog = remember {
        mutableStateOf(false)
    };
=======

@ExperimentalComposeUiApi
@Composable
fun ForumScreen(navController: NavController)
{
    Scaffold(
        topBar = {
            renderTop(
                navController,
                IconClick = {
                    navController.navigate(Screens.MainScreen.name)
                    })
                }
            ){it}
  
}


@ExperimentalComposeUiApi
@Composable
fun renderTop(navController: NavController, IconClick: () -> Unit)
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
                    Text(text = "Forum Post, Change the name later")
            }
        },
        backgroundColor = MaterialTheme.colors.onBackground,
        contentColor = MaterialTheme.colors.onSecondary
    )
>>>>>>> a284ecde409075fbeddce9d4ef116924167f605b

    val checkUserIsNull = FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty();

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            renderTopAppBar(
                title = "FORUM",
            ) {
                navController.navigate(Screens.MainScreen.name)
            }
        },
        floatingActionButton = {

            renderFloatingButtonActionForForumScreen(showDialog);


        }
    ) {
        it
    }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = {showDialog.value = false},
            title = { Text(text = "Add forum", modifier = Modifier.fillMaxWidth(),style = androidx.compose.ui.text.TextStyle(textAlign = TextAlign.Center, fontWeight = FontWeight.Bold))},
            text = { renderForm() },
            confirmButton = {
                TextButton(onClick = { showDialog.value = false }) {
                    Text(text = "OK", style = androidx.compose.ui.text.TextStyle(color = MaterialTheme.colors.onBackground))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog.value = false }) {
                    Text(text = "Cancel", style = androidx.compose.ui.text.TextStyle(color = MaterialTheme.colors.onBackground))
                }
            }
        )
    }

}


@ExperimentalComposeUiApi
@Composable
fun renderForm () {
    Column (
        modifier = Modifier.fillMaxWidth(),
            ) {
        Text("Forum")
    }
}

@ExperimentalComposeUiApi
@Composable
fun InputField(
    modifier: Modifier = Modifier,
    valueState: MutableState<String>,
    labelId: String,
    enabled: Boolean,
    isSingleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default

) {
    OutlinedTextField(
        value = valueState.value,
        onValueChange = { valueState.value = it },
        singleLine = isSingleLine,
        enabled = enabled,
        label = {
            Text(
                text = "$labelId",
                color = MaterialTheme.colors.onBackground
            )
        },
        textStyle = androidx.compose.ui.text.TextStyle(
            fontSize = 18.sp,
            color = MaterialTheme.colors.onBackground
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colors.onBackground,
            unfocusedBorderColor = Color.Gray,
            disabledBorderColor = Color.LightGray
        ),
        modifier = Modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction)
    )
}

@ExperimentalComposeUiApi
@Composable
fun renderFloatingButtonActionForForumScreen (
    showDialog: MutableState<Boolean>
) {
    FloatingActionButton(
        onClick = { showDialog.value = !showDialog.value },
        shape = RoundedCornerShape(50.dp),
        contentColor = MaterialTheme.colors.onSecondary,
        backgroundColor = MaterialTheme.colors.onBackground
    ) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Forum")
    }
}

@ExperimentalComposeUiApi
@Composable
fun renderTopAppBar (
    title: String,
    onClick: () -> Unit
) {

    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back to Main Page",
                )
            }
        },
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = "$title", style = androidx.compose.ui.text.TextStyle(fontWeight = FontWeight.Bold, fontSize = 30.sp))
            }
        },

        backgroundColor = MaterialTheme.colors.onBackground,
        contentColor = MaterialTheme.colors.onSecondary,
    )
}