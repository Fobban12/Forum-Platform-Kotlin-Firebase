package com.example.kotlin_application.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.kotlin_application.navigation.Screens

@ExperimentalComposeUiApi
@Composable
fun SearchScreen(navController: NavController) {


    Scaffold(
        topBar = {
            SearchTopBar(
                navController,
                IconClick = {
                    navController.navigate(Screens.MainScreen.name)
                })
        }
    ) { it }
}

@ExperimentalComposeUiApi
@Composable
fun SearchTopBar(navController: NavController, IconClick: () -> Unit) {
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
                SearchBar(
                    hint = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 0.dp , end = 45.dp)

                )
            }
        },
        backgroundColor = MaterialTheme.colors.onBackground,
        contentColor = MaterialTheme.colors.onSecondary
    )

}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "",
    onSearch: (String) -> Unit = {},
) {
    var text by remember {
        mutableStateOf("")
    }
    var isHintDisplayed by remember {
        mutableStateOf(hint != "")
    }

    Box(modifier = modifier) {
        BasicTextField(
            value = text,
            onValueChange = {
                text = it
                onSearch(it)
            },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp)

                    )
                    if (isHintDisplayed) {
                        Text(
                            text = hint,
                            color = Color.LightGray,
                            modifier = Modifier
                                .padding(horizontal = 20.dp, vertical = 12.dp)
                        )
                    }
                }
    }