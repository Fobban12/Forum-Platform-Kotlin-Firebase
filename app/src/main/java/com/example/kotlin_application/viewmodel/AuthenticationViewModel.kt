package com.example.kotlin_application.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin_application.data.UserProfileInput
import com.example.kotlin_application.screens.sign_in.SignInResult
import com.example.kotlin_application.screens.sign_in.SignInState
import com.example.kotlin_application.utils.CreateNotification
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.api.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@ExperimentalPermissionsApi
class AuthenticationViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    //Create database for user profile
    val userProfileDB = FirebaseFirestore.getInstance().collection("user_profile");

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    //google sign_in viewmodel from here

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    fun onSignInResult(result: SignInResult) {
        _state.update { it.copy(
            isSignInSuccessful = result.data != null,
            signInError = result.errorMessage
        ) }
    }

    fun resetState() {
        _state.update { SignInState() }
    }

    //end

    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        context: android.content.Context,
        home: () -> Unit
    ) {
        if (_loading.value == false) {
            _loading.value = true
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val uid = task.result.user?.uid;
                        val username = task.result.user?.email?.split("@")?.get(0);
                        val UserProfileInput = UserProfileInput(username, null, uid);
                        userProfileDB.add(UserProfileInput).addOnCompleteListener { it
                            if (it.isSuccessful) {
                                Log.d("Save user profile", "Save user profile successfully!");
                            } else {
                                Log.d("Save user profile", "Save user profile fail!");
                            }
                        }
                        Log.d("Firebase", "Register successfully!")
                        Toast.makeText(context, "Register successfully", Toast.LENGTH_SHORT).show()
                        home()
                    } else {
                        val exception = task.exception
                        Toast.makeText(
                            context,
                            "${exception?.message}",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.d("Firebase", "${exception?.message}")
                    }
                    _loading.value = false
                }
        }
    }

    fun signInWithEmailAndPassword(
        email: String,
        password: String,
        context: android.content.Context,
        home: () -> Unit
    ) = viewModelScope.launch {
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                        task ->
                    if (task.isSuccessful) {
                        Log.d(
                            "Firebase",
                            "Sign in with email and password: ${task.result}"
                        )
                        Toast.makeText(context, "Log In Successfully", Toast.LENGTH_SHORT).show()
                        home()
                    } else {
                        val exception = task.exception
                        Log.d("Firebase", "${exception?.message}")
                        Toast.makeText(
                            context,
                            "${exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }
        } catch (ex: Exception) {
            Log.d("Firebase", "Sign in with email and password: ${ex.message}")
            Toast.makeText(context, "Log In Fail. Please try again!", Toast.LENGTH_SHORT).show()
        }
    }
}