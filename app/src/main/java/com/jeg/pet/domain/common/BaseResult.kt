package com.jeg.pet.domain.common
sealed class BaseResult< out E: Any,out T: Any,>{
    data class Loading <T: Any>(val data: T?): BaseResult<Nothing, T>()
    data class Success <T: Any>(val data: T): BaseResult<Nothing, T>()
    data class Error <E: Any>(val rawResponse: E): BaseResult<E, Nothing>()
}