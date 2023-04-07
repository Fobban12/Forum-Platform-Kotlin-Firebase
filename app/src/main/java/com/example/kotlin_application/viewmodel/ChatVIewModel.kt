package com.example.kotlin_application.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlin_application.data.ChatInput
import com.example.kotlin_application.data.Constants
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.IllegalArgumentException
import kotlin.collections.*

class ChatVIewModel: ViewModel(){
    init {
        getMessages()
    }
    private val _message = MutableLiveData("")
    val message: LiveData<String> = _message

    //Chat collection from Firestore
    val chatDB = Firebase.firestore.collection("chat");

    private var _messages = MutableLiveData(emptyList<Map<String, Any>>().toMutableList())
    val messages: LiveData<MutableList<Map<String, Any>>> = _messages

    //Chat collection from firestore


    //Update the message value as user types

    fun updateMessage(message: String) {
        _message.value = message
}


    //Create room or add message to created room
    fun createOrUpdateRoom (userIds: List<String>, context: Context) {

        chatDB.get().addOnSuccessListener { querySnapShot ->
            var check : Boolean = false;
            querySnapShot.documents.forEach { document ->
                val arrayUserIds : List<String> = document.get("userIds") as ArrayList<String>;
                Log.d("arrayOfUserIds", "${arrayUserIds} and ${userIds}")
                if (userIds.all { it -> arrayUserIds.contains(it) }) {
                    check = true

                }
            }

            var newText : String = "";
            userIds.forEachIndexed { index, it ->
                if (index == userIds.size - 1) {
                    newText += "user with id: " + it + "";
                } else {
                    newText += "user with id: " + it + " and "
                }
            }


            if (!check) {
                val newChatRoom = ChatInput(userIds = userIds, messages = listOf(), Timestamp.now());
                chatDB.add(newChatRoom).addOnSuccessListener {
                    Log.d("Create new room", "Create a new room successfully!");
                    Toast.makeText(context, "Room not yet created! Create new room! Move to a single chat screen of ${newText}", Toast.LENGTH_LONG).show();
                }
            } else {

                Toast.makeText(context, "Move to a single chat screen of ${newText}", Toast.LENGTH_LONG).show();
            }


        }

    }



//send message

fun addMessage() {
    val message: String = _message.value ?: throw IllegalArgumentException("message empty")
    if (message.isNotEmpty()) {
        Firebase.firestore.collection(Constants.MESSAGES).document().set(
            hashMapOf(
                Constants.MESSAGE to message,
                Constants.SENT_BY to Firebase.auth.currentUser?.uid,
                Constants.SENT_ON to System.currentTimeMillis()
            )
        ).addOnSuccessListener {
            _message.value = ""
        }
    }
}

    /**
     * Get the messages
     */

    private fun getMessages() {
        Firebase.firestore.collection(Constants.MESSAGES)
            .orderBy(Constants.SENT_ON)
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.w(Constants.TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                val list = emptyList<Map<String, Any>>().toMutableList()

                if (value != null) {
                    for (doc in value) {
                        val data = doc.data
                        data[Constants.IS_CURRENT_USER] =
                            Firebase.auth.currentUser?.uid.toString() == data[Constants.SENT_BY].toString()

                        list.add(data)
                    }
                }

                updateMessages(list)
            }
    }

    /**
     * Update the list after getting the details from firestore
     */

    private fun updateMessages(list: MutableList<Map<String, Any>>) {
        _messages.value = list.asReversed()
    }
}
