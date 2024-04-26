package com.rodcollab.gymmateapp.core

sealed interface ResultOf<out T>  {
    data class Success<out R>(val value: R): ResultOf<R>
    data object Idle : ResultOf<Nothing>
    data class Failure(
        val message: String?,
        val throwable: Throwable?
    ): ResultOf<Nothing>
}