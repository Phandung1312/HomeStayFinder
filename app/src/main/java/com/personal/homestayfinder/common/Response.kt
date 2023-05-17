package com.personal.homestayfinder.common

sealed class Response<out T> {
    data class Success<out T>(
        val data: T,
    ) : Response<T>()

    data class Error(
        val message: String,
    ) : Response<Nothing>();
}