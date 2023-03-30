package com.example.kotlin_application.viewmodel

import androidx.compose.runtime.Composable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlin_application.data.Forum
import com.example.kotlin_application.data.UserProfile
import com.google.firebase.firestore.FirebaseFirestore


class UserProfileViewModel : ViewModel() {

    //Database for user profile from firestore
    val userProfileDB = FirebaseFirestore.getInstance().collection("user_profile");
    private val _singleUserProfile = MutableLiveData<UserProfile?>(null);
    val singleUserProfile: MutableLiveData<UserProfile?> = _singleUserProfile;

    //Fetch single user profile
    fun fetchSingleUserProfile (userId : String) {

        userProfileDB.whereEqualTo("userId", userId).get().addOnSuccessListener { querySnapshot ->

            querySnapshot.documents.map { it ->
                val singleUser = UserProfile(it.id, it.getString("username").toString(), it.getString("image").toString(), it.getString("userId").toString());
                singleUserProfile.value = singleUser;
            }
        }
    }


}