package com.example.kotlin_application.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin_application.data.Forum
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class ForumViewModel : ViewModel() {


    //Test with dummy data from firestore. Will complete when Jere completes Post for Forum
    var forum = mutableStateListOf<Forum>()


    init {
        getForum()
    }

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