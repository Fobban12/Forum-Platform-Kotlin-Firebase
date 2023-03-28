package com.example.kotlin_application.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin_application.data.Forum
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ForumViewModel : ViewModel() {


    //Test with dummy data from firestore. Will complete when Jere completes Post for Forum

    var forum = mutableStateListOf<Forum>()
    private val _singleForum = MutableLiveData<Forum?>(null)
    val singleForum: MutableLiveData<Forum?> = _singleForum

    //Like database from Firestore
    val like = FirebaseFirestore.getInstance().collection("like");

    //Comment database from Firestore
    val comment = FirebaseFirestore.getInstance().collection("comment");

    //Like for comment database from Firestore
    val like_for_comment = FirebaseFirestore.getInstance().collection("like_for_comment");

    init {
        getForum()
    }

    //Fetch single forum
    fun getSingleForum (forumId : String) {
        viewModelScope.launch {
            Firebase.firestore.collection("forum").document(forumId).get()
                .addOnSuccessListener { documentSnapshot ->

                    if (documentSnapshot.exists()) {
                        var forum = Forum(documentSnapshot.id, documentSnapshot.getString("title").toString(), documentSnapshot.getString("type").toString(), documentSnapshot.getString("description").toString(), documentSnapshot.getString("image").toString(),documentSnapshot.getDate("createdAt"), documentSnapshot.getString("userId"), documentSnapshot.getString("username"));
                        singleForum.value = forum;
                    }


                }
                .addOnFailureListener {

                }
        }
    }

    //Fetch all forums
    fun getForum(){
        viewModelScope.launch {
            Firebase.firestore.collection("forum")
                .get()
                .addOnSuccessListener {
                    val forums = mutableListOf<Forum>()
                    it.documents.forEach { doc ->
                        var forum = Forum(doc.id, doc.getString("title").toString(), doc.getString("type").toString(), doc.getString("description").toString(), doc.getString("image").toString(),doc.getDate("createdAt"), doc.getString("userId"), doc.getString("username"))
                        forums.add(forum)
                    }
                    forum.clear();
                    forum.addAll(forums)
                }
                .addOnFailureListener {  }
        }
    }

    //Delete forum
    fun deleteForum (forumId: String, context: Context) {
        viewModelScope.launch {
            Firebase.firestore.collection("forum").document(forumId).delete();
            forum.removeIf { it.id == forumId };
            Toast.makeText(context, "Remove forum successfully!", Toast.LENGTH_LONG).show()

            //Delete like of forum
            like.whereEqualTo("forumId", forumId).get().addOnSuccessListener { snapShot ->
                for (document in snapShot.documents) {
                    document.reference.delete().addOnSuccessListener {
                        Log.d("Delete like of forum", "Delete like of forum successfully!")
                    }
                        .addOnFailureListener {
                            Log.d("Delete like of forum", "Delete fail")
                        }
                }
            }

            //Delete comment of forum
            comment.whereEqualTo("forumId", forumId).get().addOnSuccessListener { snapShot ->
                for (document in snapShot.documents) {
                    document.reference.delete().addOnSuccessListener {
                        Log.d("Delete comment of forum", "Delete comment of forum successfully!")
                    }
                        .addOnFailureListener {
                            Log.d("Delete comment of forum", "Delete fail")
                        }
                }
            }

            //Delete like for single comment of forum
            like_for_comment.whereEqualTo("forumId", forumId).get().addOnSuccessListener { snapShot ->
                for (document in snapShot.documents) {
                    document.reference.delete().addOnSuccessListener {
                        Log.d("Delete like  of forum", "Delete comment of forum successfully!")
                    }
                        .addOnFailureListener {
                            Log.d("Delete comment of forum", "Delete fail")
                        }
                }
            }

        }
    }

}