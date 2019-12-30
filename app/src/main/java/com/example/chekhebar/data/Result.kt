package com.example.chekhebar.data

sealed class Result<out T> {

    data class Loading<out T>(val data: T) : Result<T>()

    data class Success<out T>(val data: T) : Result<T>()

    data class Error<out T>(val message: String, val data: T? = null) : Result<T>()

    override fun toString(): String {
        return when (this) {
            is Loading<*> -> "Loading[data=$data]"
            is Success<*> -> "Success[data=$data]"
            is Error<*> -> "Error[exception=$message, data=$data]"
        }
    }
}