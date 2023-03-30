package com.example.kotlin_application.screens.authentication

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.kotlin_application.navigation.Screens
import com.example.kotlin_application.viewmodel.AuthenticationViewModel
import androidx.compose.material.TextFieldDefaults
import com.google.firebase.auth.FirebaseAuth


@Composable
@ExperimentalComposeUiApi
fun LoginScreen(
    navController: NavController,
    viewModel: AuthenticationViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    isRegister: String
) {

    //Check user is null
    val checkUserIsNull = remember(FirebaseAuth.getInstance().currentUser?.email) {
        FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()
    }

    //Set state for log in form
    val isLoginForm = rememberSaveable {
        mutableStateOf(if (isRegister == "true") false else true)
    }

    //Set context for toast
    val context = LocalContext.current


    //Set effect to check user is logged in and now allowed to access log in page
    LaunchedEffect(checkUserIsNull, isLoginForm.value) {
        if (!checkUserIsNull && isLoginForm.value == true) {
            navController.navigate(Screens.MainScreen.name);
            Toast.makeText(context, "You are logged in already!", Toast.LENGTH_LONG).show();
        }
    }


    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = if (isLoginForm.value) "Log In Form" else "Register",
                modifier = Modifier.padding(10.dp),
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.onBackground
                )
            )

            if (!isLoginForm.value) {
                Text(
                    text = "Please enter a valid email with min length 3 and password with min length 8!\nPassword must have one digit, one lowercase letter, one uppercase letter, one special character, and has no whitespace characters!",
                    modifier = Modifier.padding(5.dp),
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.onBackground
                    )
                )
            } else {
                Text(
                    text = ""
                )
            }
            if (isLoginForm.value) {
                UserForm(loading = false, isCreateAccount = false) {
                        email, password ->
                    viewModel.signInWithEmailAndPassword(
                        email = email,
                        password = password,
                        context = context
                    ) {
                        navController.navigate(Screens.MainScreen.name)
                    }
                    Log.d("Form", "Login Screen: $email $password")
                }
            } else {
                UserForm(loading = false, isCreateAccount = true) {
                        email, password ->
                    viewModel.createUserWithEmailAndPassword(email, password, context) {
                        isLoginForm.value = !isLoginForm.value
                    }
                    Log.d("Form", "Register Screen: $email @password")
                }
            }

            Row(
                modifier = Modifier.padding(3.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isLoginForm.value) {
                    Text(
                        text = "Don't you have an account?",
                        style = androidx.compose.ui.text.TextStyle(
                            color = MaterialTheme.colors.onSecondary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                } else {
                    Text(
                        text = "Have an account already?",
                        style = androidx.compose.ui.text.TextStyle(
                            color = MaterialTheme.colors.onSecondary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                Text(
                    text = if (isLoginForm.value) "Please register!" else "Please sign in",
                    modifier = Modifier
                        .clickable {
                            isLoginForm.value = !isLoginForm.value
                        }
                        .padding(2.dp),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.onBackground
                )
            }
            Spacer(modifier = Modifier.height(50.dp))
            Text(text = "Go To Main Page", modifier = Modifier.clickable { navController.navigate(Screens.MainScreen.name)}, style = androidx.compose.ui.text.TextStyle(color = MaterialTheme.colors.onBackground, fontWeight = FontWeight.Bold))
        }
    }
}

@ExperimentalComposeUiApi
@Preview
@Composable
fun UserForm(
    loading: Boolean = false,
    isCreateAccount: Boolean = false,
    onDone: (String, String) -> Unit = { email, pwd -> }
) {
    val email = rememberSaveable {
        mutableStateOf("")
    }

    val password = rememberSaveable {
        mutableStateOf("")
    }

    val passwordVisibility = rememberSaveable {
        mutableStateOf(false)
    }

    val navController = rememberNavController()

    val passwordFocusRequest = FocusRequester.Default
    val keyboardController = LocalSoftwareKeyboardController.current

//    one digit (0-9)
//    one lowercase letter (a-z)
//    one uppercase letter (A-Z)
//    one special character (@#$%^&+=)
//    has no whitespace characters
//            and is at least 8 characters long.

    val passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!?¤/@#\$%^&+=])(?=\\S+\$).{8,}\$".toRegex()

    val valid = remember(email.value, password.value) {
        email.value.trim().isNotEmpty() && email.value.trim().contains("@") && email.value.trim().length >= 3 && password.value.trim().isNotEmpty() && password.value.matches(
            passwordRegex
        )
    }

    val modifier = Modifier
        .height(250.dp)
        .background(MaterialTheme.colors.background)
        .verticalScroll(rememberScrollState())

    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        emailInput(
            emailState = email,
            enabled = true,
            onAction = KeyboardActions {
                passwordFocusRequest.requestFocus()
            }
        )

        passwordInput(
            modifier = Modifier.focusRequester(passwordFocusRequest),
            isSingleLine = true,
            passwordVisibility = passwordVisibility,
            passwordState = password,
            enabled = true,
            onAction = KeyboardActions(onDone = {
//                if (password.value.length == 0) return@KeyboardActions
                keyboardController?.hide()
                onDone(email.value.trim(), password.value.trim())
            })
        )

        SubmitButton(
            textId = if (isCreateAccount) "Create Account" else "Log In",
            loading = loading,
            validInputs = valid
        ) {
            onDone(email.value.trim(), password.value.trim())
            keyboardController?.hide()
            Log.d("Press successfully!", "Print")
        }
    }
}

@Composable
fun SubmitButton(
    textId: String,
    loading: Boolean,
    validInputs: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.onBackground,
            contentColor = Color.White
        ),
        enabled = !loading && validInputs,
        shape = CircleShape
    ) {
        if (loading) {
            CircularProgressIndicator(modifier = Modifier.size(25.dp))
        } else {
            Text(text = textId, modifier = Modifier.padding(5.dp))
        }
    }
}

@Composable
fun passwordInput(
    modifier: Modifier = Modifier,
    passwordState: MutableState<String>,
    labelId: String = "Password",
    enabled: Boolean = true,
    isSingleLine: Boolean,
    passwordVisibility: MutableState<Boolean>,
    imeAction: ImeAction = ImeAction.Done,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    val visualTransformation = if (passwordVisibility.value) {
        VisualTransformation.None
    } else {
        PasswordVisualTransformation()
    }

    OutlinedTextField(
        value = passwordState.value, onValueChange = { passwordState.value = it }, label = {
            Text(
                text = "$labelId",
                color = MaterialTheme.colors.onBackground
            )
        }, enabled = enabled, singleLine = isSingleLine,
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
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        ),
        keyboardActions = onAction, visualTransformation = visualTransformation, trailingIcon = {
            PasswordVisibility(
                passwordVisibility = passwordVisibility
            )
        }
    )
}

@Composable
fun PasswordVisibility(passwordVisibility: MutableState<Boolean>) {
    val passwordVisibilityValue = passwordVisibility.value

    val image = if (passwordVisibility.value) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
    IconButton(onClick = { passwordVisibility.value = !passwordVisibilityValue }) {
        Icon(
            imageVector = image,
            contentDescription = if (passwordVisibility.value) "Hide Password" else "Show Password"
        )
        Icons.Default.Close
    }
}

@Composable
fun emailInput(
    modifier: Modifier = Modifier,
    emailState: MutableState<String>,
    labelId: String = "Email",
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    InputField(
        modifier,
        emailState,
        labelId,
        enabled,
        keyboardType = KeyboardType.Email,
        imeAction = imeAction,
        onAction = onAction
    )
}

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