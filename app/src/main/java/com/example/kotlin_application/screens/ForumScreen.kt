package com.example.kotlin_application.screens
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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*

import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu

import androidx.compose.runtime.rememberCoroutineScope

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

import java.time.format.TextStyle

@ExperimentalComposeUiApi
@Composable
fun ForumScreen(navController: NavController) {



    Scaffold(
        topBar = {
            renderTopBarRead(
                navController,
                IconClick = {
                    navController.navigate(Screens.MainScreen.name)
                })
        }
    ){it}
}

@ExperimentalComposeUiApi
@Composable
fun renderTopBarRead(navController: NavController, IconClick: () -> Unit)
{
    val scaffoldState = rememberScaffoldState();

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
                    Text(text = "Forum Read")
            }
        },
        backgroundColor = MaterialTheme.colors.onBackground,
        contentColor = MaterialTheme.colors.onSecondary
    )



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


