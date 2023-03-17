package com.example.kotlin_application.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.compose.material.Snackbar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.api.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.util.logging.Handler


class AuthenticationViewModel: ViewModel()  {
    private val auth: FirebaseAuth = Firebase.auth


    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading


    fun createUserWithEmailAndPassword (email: String, password: String, home: () -> Unit) {
        if (_loading.value == false) {
            _loading.value = true
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("Firebase", "Register successfully!")
                        home()
                    } else {
                        Log.d("Firebase", "Register fail!")
                    }
                    _loading.value = false
                }
        }
    }

    fun signInWithEmailAndPassword (email: String, password: String, home: () -> Unit) = viewModelScope.launch {
        try {

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    task ->
                    if (task.isSuccessful) {
                        Log.d("Firebase", "Sign in with email and password: ${task.result.toString()}")
                        home()
                    } else {
                        Log.d("Firebase", "Sign in with email and password fail!")
                    }
                }


        } catch (ex: Exception) {
            Log.d("Firebase", "Sign in with email and password: ${ex.message}")
        }
    }


}