package com.example.kotlin_application.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin_application.data.Forum
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ForumViewModel : ViewModel() {


    //Test with dummy data from firestore. Will complete when Jere completes Post for Forum

    var forum = mutableStateListOf<Forum>()
    private val _singleForum = MutableLiveData<Forum?>(null)
    val singleForum: MutableLiveData<Forum?> = _singleForum


    init {
        getForum()
    }

    //Fetch single forum
    fun getSingleForum (forumId : String) {
        viewModelScope.launch {
            Firebase.firestore.collection("forum").document(forumId).get()
                .addOnSuccessListener { documentSnapshot ->

                    if (documentSnapshot.exists()) {
                        var forum = Forum(documentSnapshot.id, documentSnapshot.getString("title").toString(), documentSnapshot.getString("type").toString(), documentSnapshot.getString("description").toString(), documentSnapshot.getString("image").toString(),documentSnapshot.getDate("createdAt"), documentSnapshot.getString("userId"));
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
                        var forum = Forum(doc.id, doc.getString("title").toString(), doc.getString("type").toString(), doc.getString("description").toString(), doc.getString("image").toString(),doc.getDate("createdAt"), doc.getString("userId"))
                        forums.add(forum)
                    }
                    forum.clear();
                    forum.addAll(forums)
                }
                .addOnFailureListener {  }
        }
    }

}