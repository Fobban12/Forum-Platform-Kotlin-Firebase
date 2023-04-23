package com.example.kotlin_application.viewmodel

import android.content.Context
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin_application.data.Forum
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.launch
import java.lang.ref.Reference
import java.util.*
import java.util.logging.Handler


class ForumViewModel : ViewModel() {


    //Test with dummy data from firestore. Will complete when Jere completes Post for Forum

    val forumDB = FirebaseFirestore.getInstance().collection("forum")

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
                        var forum = Forum(documentSnapshot.id, documentSnapshot.getString("idImage").toString(),documentSnapshot.getString("title").toString(), documentSnapshot.getString("type").toString(), documentSnapshot.getString("description").toString(), documentSnapshot.getString("image").toString(),documentSnapshot.getTimestamp("createdAt"), documentSnapshot.getString("userId"), documentSnapshot.getString("username"));
                        singleForum.value = forum;
                    }


                }
                .addOnFailureListener {

                }
        }
    }

    //Fetch all forums
    private fun getForum(){
        viewModelScope.launch {
            Firebase.firestore.collection("forum")
                .get()
                .addOnSuccessListener {
                    val forums = mutableListOf<Forum>()
                    it.documents.forEach { doc ->
                        var forum = Forum(doc.id, doc.getString("idImage").toString(),doc.getString("title").toString(), doc.getString("type").toString(), doc.getString("description").toString(), doc.getString("image").toString(),doc.getTimestamp("createdAt"), doc.getString("userId"), doc.getString("username"))
                        forums.add(forum)
                    }
                    forum.clear();
                    forum.addAll(forums)
                }
                .addOnFailureListener {  }
        }
    }

    //Delete forum
    fun deleteForum (forumId: String, context: Context, userId: String, imageID: String) {
        //Get storage reference
        val ref: StorageReference = FirebaseStorage.getInstance().reference
        //Get the exact location of the image
        val getRef = ref.child("/users/$userId/forum/$imageID/image")
        //Get ID
        viewModelScope.launch {
            Firebase.firestore.collection("forum").document(forumId).delete();
        //Remove Forum if the user ID is correct
            forum.removeIf { it.id == forumId };
            Toast.makeText(context, "Forum successfully deleted", Toast.LENGTH_LONG).show()

            //Delete Image from storage
            getRef.delete()

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

    //Search forum based on keywords
    fun searchForums (keyword : String) {
        viewModelScope.launch {
            FirebaseFirestore.getInstance().collection("forum").get().addOnSuccessListener {
                val forums = mutableListOf<Forum>()
                it.documents.forEach { doc ->
                    var forum = Forum(doc.id, doc.getString("idImage").toString(),doc.getString("title").toString(), doc.getString("type").toString(), doc.getString("description").toString(), doc.getString("image").toString(),doc.getTimestamp("createdAt"), doc.getString("userId"), doc.getString("username"))
                    forums.add(forum)
                }

                val subKeywords : List<String> = keyword.split(" ").map { it.trim() };
                Log.d("size", "${subKeywords.size}")
                val filteredForums = forums.filter {
                        it ->

                    var check : Boolean = false;

                    subKeywords.forEach { item ->
                        if (it?.title?.trim()?.toLowerCase()?.contains(item.toString().trim().toLowerCase()) as Boolean || it?.description?.trim()?.toLowerCase()?.contains(item.toString().trim().toLowerCase()) as Boolean) {
                            check = true;
                        }}
                    check;
                }


                forum.clear();
                forum.addAll(filteredForums);
            }
                .addOnFailureListener {

                }


        }
    }

    //For sending data to the database when a forum page is created by the User
    fun createForum(forum:Forum, context: Context){
        viewModelScope.launch {
            forumDB.add(forum).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Added a new Forum!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "Failed to make a Forum", Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener {
                Toast.makeText(context, "Failed to make a Forum!", Toast.LENGTH_LONG).show()
            }
        }
    }

    }

