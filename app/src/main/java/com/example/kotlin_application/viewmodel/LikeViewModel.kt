package com.example.kotlin_application.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin_application.data.Like
import com.example.kotlin_application.data.LikeInput
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class LikeViewModel : ViewModel() {

    val likeDB = FirebaseFirestore.getInstance().collection("like");
    val likes = mutableStateListOf<Like?>();

    fun saveLike (likeInput: LikeInput, context: Context) {
        viewModelScope.launch {
            likeDB.add(likeInput).addOnCompleteListener { it ->
                if (it.isSuccessful) {
                    Toast.makeText(context, "You liked this forum!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "You liked this forum fail!", Toast.LENGTH_LONG).show();
                }

            }
        }
    }
}
