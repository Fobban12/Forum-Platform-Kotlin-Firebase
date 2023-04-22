package com.example.kotlin_application.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin_application.data.Comment
import com.example.kotlin_application.data.Forum
import com.example.kotlin_application.data.Message
import com.example.kotlin_application.data.UserProfile
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.concurrent.Flow


class UserProfileViewModel : ViewModel() {

    //Database for user profile from firestore
    val userProfileDB = FirebaseFirestore.getInstance().collection("user_profile");
    private val _singleUserProfile = MutableLiveData<UserProfile?>(null);
    val singleUserProfile: MutableLiveData<UserProfile?> = _singleUserProfile;

    //Database for Forum
    val usersForums = FirebaseFirestore.getInstance().collection("forum")
    //Database for Comments
    val usersComments = FirebaseFirestore.getInstance().collection("comment")

    //Fetch single user profile
    fun fetchSingleUserProfile (userId : String) {

        userProfileDB.whereEqualTo("userId", userId).get().addOnSuccessListener { querySnapshot ->

            querySnapshot.documents.map { it ->
                val singleUser = UserProfile(it.id, it.getString("username").toString(), it.getString("image").toString(), it.getString("userId").toString());
                singleUserProfile.value = singleUser;
            }
        }
    }

    //Fetch single user profile with promise yield
     suspend fun fetchSingleUserProfileWithPromiseYield (userId: String) : UserProfile ?= withContext(Dispatchers.IO) {
        var singleUserProfile: UserProfile? = null // initialize singleUserProfile to null
        val querySnapshot = userProfileDB.whereEqualTo("userId", userId).get().await()
        querySnapshot.documents.mapNotNull { documentSnapshot ->
            UserProfile(
                documentSnapshot.id,
                documentSnapshot.getString("username").orEmpty(),
                documentSnapshot.getString("image").orEmpty(),
                documentSnapshot.getString("userId").orEmpty()
            )
        }.singleOrNull()

    }

    //Fetch single user profile based on single user profile id
    fun fetchSingleUserProfileByProfileId (userProfileId: String) {
        viewModelScope.launch {
            userProfileDB.document(userProfileId).get().addOnSuccessListener { querySnapshot ->
                if (querySnapshot.exists()) {
                    val userProfile = UserProfile(querySnapshot.id, querySnapshot.getString("username").toString(), querySnapshot.getString("image").toString(), querySnapshot.getString("userId").toString());
                    singleUserProfile.value = userProfile;
                }

            }
        }
    }

    //Update new username profile based on single user profile id to update username
    fun updateUsername (userProfileId: String, usernameInput : String, context: Context, userId:String) {

        viewModelScope.launch {


            //Update username from Comments
            usersComments.whereEqualTo("userId",userId).get().addOnSuccessListener {
                for (document in it)
                {
                    val newUsername = Comment(document.id,document.getString("comment").toString(),document.getTimestamp("createdAt"),document.getString("forumId").toString(),userId,usernameInput)

                    usersComments.document(document.id).set(newUsername)

                }
            }

            //Update username from Forums
            usersForums.whereEqualTo("userId",userId).get().addOnSuccessListener {
                for (document in it)
                {
                    val newUsername = Forum(document.id,document.getString("idImage").toString(),document.getString("title").toString()
                        ,document.getString("type").toString(),document.getString("description").toString()
                    ,document.getString("image").toString(),document.getTimestamp("createdAt")
                    ,userId,usernameInput)

                   usersForums.document(document.id).set(newUsername)

                }
            }
            
            userProfileDB.document(userProfileId).get().addOnSuccessListener { querySnapshot ->
                if (querySnapshot.exists()) {
                    val updatedUsername = UserProfile(querySnapshot.id, usernameInput, querySnapshot.getString("image").toString(), querySnapshot.getString("userId").toString());
                    userProfileDB.document(userProfileId).set(updatedUsername).addOnSuccessListener {
                        Toast.makeText(context, "Update username successfully", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    //Update new username profile based on single user profile id to update username
    fun updateImage (userProfileId: String, imageUrl : String, context: Context) {
        viewModelScope.launch {
            userProfileDB.document(userProfileId).get().addOnSuccessListener { querySnapshot ->
                if (querySnapshot.exists()) {
                    val updatedUsername = UserProfile(querySnapshot.id, querySnapshot.getString("username").toString(), image = imageUrl, querySnapshot.getString("userId").toString());
                    userProfileDB.document(userProfileId).set(updatedUsername).addOnSuccessListener {
                        Toast.makeText(context, "Update image for user profile successfully", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }




}