package com.example.kotlin_application.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin_application.data.LikeForComment
import com.example.kotlin_application.data.LikeForCommentInput
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class LikeForCommentModel : ViewModel() {

    val likeForCommentDB = Firebase.firestore.collection("like_for_comment");
    val likesForComment = mutableStateListOf<LikeForComment?>();

    //Save like to comment
    fun saveLikeToComment (likeForCommentInput: LikeForCommentInput, context: Context) {
        viewModelScope.launch {
            likeForCommentDB.add(likeForCommentInput).addOnCompleteListener { it ->
                if (it.isSuccessful) {
                    val newLikeForComment = LikeForComment(it.result.id, likeForCommentInput.userId.toString(), likeForCommentInput.username.toString(), likeForCommentInput.forumId.toString(), likeForCommentInput.commentId.toString())
                    likesForComment.add(newLikeForComment);
                    Toast.makeText(context, "Like comment succesfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Like comment fail", Toast.LENGTH_LONG).show();
                }

            }
        }
    }

    //Fetch likes from single comment
    fun fetchLikesForSingleComment (forumId: String, commentId : String) {
        viewModelScope.launch {
            likeForCommentDB.whereEqualTo("forumId", forumId).whereEqualTo("commentId", commentId).get().addOnSuccessListener { result ->
                val listOfLikeForComment = mutableStateListOf<LikeForComment?>();
                result.documents.map { it ->
                    val likeForComment = LikeForComment(it.id, it.getString("userId").toString(), it.getString("username").toString(), it.getString("forumId").toString(), it.getString("commentId").toString());
                    listOfLikeForComment.add(likeForComment);
                }

                likesForComment.clear();
                likesForComment.addAll(listOfLikeForComment);
            }
        }
    }

    //Delete like
    fun deleteLikeForComment (id: String, context: Context) {
        viewModelScope.launch {
            likeForCommentDB.document(id).delete();
            likesForComment.removeIf { it?.id == id};
            Toast.makeText(context, "Dislike successfully!", Toast.LENGTH_LONG).show();
        }
    }

}