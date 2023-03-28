package com.example.kotlin_application.data

// to process the response that we get from Cloud FireStore...

sealed class Response<out T> {
    object Loading : Response<Nothing>()

    data class Success<out T>(
        val data: T
    ) : Response<T>()

    data class Error(
        val message: String
    ) : Response<Nothing>()

}
