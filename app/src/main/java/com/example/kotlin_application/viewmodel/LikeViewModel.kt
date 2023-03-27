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


    fun checkLike (forumId: String, uid : String) : Boolean {
        return likes.any { it?.forumId == forumId && it?.userId == uid }
    }

    fun saveLike (likeInput: LikeInput, context: Context) {
        viewModelScope.launch {
            likeDB.add(likeInput).addOnCompleteListener { it ->
                if (it.isSuccessful) {
                    val newLike = Like(it.result.id, likeInput.forumId, likeInput.userId, likeInput.username);
                    likes.add(newLike);
                    Toast.makeText(context, "You liked this forum!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "You liked this forum fail!", Toast.LENGTH_LONG).show();
                }

            }
        }
    }

    fun fetchLikes (forumId: String) {
        viewModelScope.launch {
            likeDB.whereEqualTo("forumId", forumId).get().addOnSuccessListener { result ->
                val likeList = mutableStateListOf<Like?>();

                result.documents.map { like ->
                    val newLike = Like(like.id, like.getString("forumId").toString(), like.getString("userId").toString(), like.getString("username").toString());
                    likeList.add(newLike);
                }
                likes.clear();
                likes.addAll(likeList);
            }
        }
    }

    fun deleteLike (id : String, context: Context) {
        viewModelScope.launch {
            likeDB.document(id).delete();
            likes.removeIf { it?.id == id}
            Toast.makeText(context, "Dislike successfully", Toast.LENGTH_LONG).show()
        }
    }
}
