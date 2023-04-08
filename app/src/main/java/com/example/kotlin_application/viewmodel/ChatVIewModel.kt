package com.example.kotlin_application.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin_application.data.*
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
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

    //Message collection from Firestore
    val messageDB = Firebase.firestore.collection("message_in_chat")

    private var _messages = MutableLiveData(emptyList<Map<String, Any>>().toMutableList())
    val messages: LiveData<MutableList<Map<String, Any>>> = _messages

    //Set value for single chat room
    private val _singleChatroom = MutableLiveData<Chat?>(null)
    val singleChatroom: MutableLiveData<Chat?> = _singleChatroom;

    //Set value for single message
    private val _singleMessage = MutableLiveData<Message?>(null)
    val singleMessage: MutableLiveData<Message?> = _singleMessage;

    //Set state for chat room array
    val chatRooms = mutableStateListOf<Chat?>();

    //Update the message value as user types

    fun updateMessage(message: String) {
        _message.value = message
}
    //Add messages to a single specific chat room
    fun addMessagesToChatRoom (messageInput: MessageInput, chatId: String, context: Context) {

        viewModelScope.launch {
            messageDB.add(messageInput).addOnSuccessListener { it ->
                val createdMessage = Message(it.id, messageInput.content, messageInput.senderId, messageInput.createdAt);
                chatDB.document(chatId).get().addOnSuccessListener { it ->
                    val arrayMessages = it.get("messages") as List<String>;
                    arrayMessages.plus(createdMessage.id);
                    singleChatroom.value?.messages?.plus(createdMessage.id);
                    val newChatUpdateWithMessages = Chat(it.id, it.get("userIds") as List<String>, arrayMessages, it.getTimestamp("createdAt"));
                    chatDB.document(chatId).set(newChatUpdateWithMessages).addOnCompleteListener { it ->
                        if (it.isSuccessful) {
                            Toast.makeText(context, "Add message successfully", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        }
    }

    //Fetch single message based on message id
    fun fetchSingleMessage (messageId : String , context: Context) {
        viewModelScope.launch {
            messageDB.document(messageId).get().addOnSuccessListener { it ->
                val message = Message(it.id, it.getString("content") as String, it.getString("senderId") as String, it.getTimestamp("createdAt") as Timestamp);
                singleMessage.value = message;
            }
        }
    }

    //Fetch room based on userIds array string
    fun fetchRoomBasedOnUserIds (userIds: List<String>, context: Context) {
        viewModelScope.launch {
            Log.d("Fetch Room", "Based Room")

            chatDB.whereIn("userIds", userIds).get().addOnSuccessListener { querySnapshot ->
                val documents = querySnapshot.documents;

                val getChatRooms = mutableStateListOf<Chat?>();
                for (document in documents) {
                    val chat = Chat(document.id, document.get("userIds") as List<String>, document.get("messages") as List<String>, document.getTimestamp("createdAt"));
                    getChatRooms.add(chat);
                }

                chatRooms.clear();
                chatRooms.addAll(getChatRooms);
            }
        }

    }

    //Fetch room based on chat id
    fun fetchRoomWithRoomId (chatId : String) {
        viewModelScope.launch {
            chatDB.document(chatId).get().addOnSuccessListener { it ->
                val chat = Chat(it.id, it.get("userIds") as List<String>, it.get("messages") as List<String>, it.getTimestamp("createdAt"));
                chatRooms.clear();
                chatRooms.add(chat);
            }
        }
    }


    //Create room or add message to created room
    fun createOrUpdateRoom (userIds: List<String>, context: Context) {

        viewModelScope.launch {
            chatDB.get().addOnSuccessListener { querySnapShot ->
                var check: Boolean = false;
                querySnapShot.documents.forEach { document ->
                    val arrayUserIds: List<String> = document.get("userIds") as ArrayList<String>;
                    Log.d("arrayOfUserIds", "${arrayUserIds} and ${userIds}")
                    if (userIds.all { it -> arrayUserIds.contains(it) }) {
                        check = true

                    }
                }

                var newText: String = "";
                userIds.forEachIndexed { index, it ->
                    if (index == userIds.size - 1) {
                        newText += "user with id: " + it + "";
                    } else {
                        newText += "user with id: " + it + " and "
                    }
                }


                if (!check) {
                    val newChatRoom =
                        ChatInput(userIds = userIds, messages = listOf(), Timestamp.now());
                    chatDB.add(newChatRoom).addOnSuccessListener {
                        Log.d("Create new room", "Create a new room successfully!");
                        Toast.makeText(
                            context,
                            "Room not yet created! Create new room! Move to a single chat screen of ${newText}",
                            Toast.LENGTH_LONG
                        ).show();

                    }
                } else {
                    Toast.makeText(
                        context,
                        "Move to a single chat screen of ${newText}",
                        Toast.LENGTH_LONG
                    ).show();
                }




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
