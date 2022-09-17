package com.jeg.pet.data.common.utils

import com.squareup.moshi.JsonEncodingException
import okhttp3.internal.http2.Http2ExchangeCodec
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.HttpURLConnection
import javax.net.ssl.HttpsURLConnection

class ErrorHandler<T> {

    private fun handleError(source: DataSource): WrappedResponse<T> {
        return source.getErrorResponse()
    }

    fun handleError(responseCode: Int): WrappedResponse<T> {
        return when (responseCode) {
            ResponseCode.SUCCESS or ResponseCode.NO_CONTENT -> handleError(DataSource.SUCCESS)
            ResponseCode.NOT_FOUND -> handleError(DataSource.NOT_FOUND)
            else -> handleError(DataSource.DEFAULT)

        }
    }

    fun handleException(exception: Throwable): WrappedResponse<T> {
        return when (exception) {
            is ConnectException -> handleError(DataSource.NO_INTERNET_CONNECTION)
            is JsonEncodingException -> handleError(DataSource.BAD_RESPONSE)
            is IOException -> handleError(DataSource.BAD_RESPONSE)
            else -> handleError(DataSource.DEFAULT)
        }
    }
}

enum class DataSource {
    SUCCESS,
    NO_CONTENT,
    BAD_REQUEST,
    FORBIDDEN,
    UNAUTHORISED,
    NOT_FOUND,
    INTERNAL_SERVER_ERROR,
    CONNECT_TIMEOUT,
    CANCEL,
    RECEIVE_TIMEOUT,
    SEND_TIMEOUT,
    CACHE_ERROR,
    BAD_RESPONSE,
    NO_INTERNET_CONNECTION,
    DEFAULT,

}

fun <T> DataSource.getErrorResponse(): WrappedResponse<T> {
    when (this) {
        DataSource.SUCCESS ->
            return WrappedResponse(ResponseCode.SUCCESS, ResponseMessage.SUCCESS)
        DataSource.NO_CONTENT ->
            return WrappedResponse(ResponseCode.NO_CONTENT, ResponseMessage.NO_CONTENT)
        DataSource.BAD_REQUEST ->
            return WrappedResponse(ResponseCode.BAD_REQUEST, ResponseMessage.BAD_REQUEST)
        DataSource.FORBIDDEN ->
            return WrappedResponse(ResponseCode.FORBIDDEN, ResponseMessage.FORBIDDEN)
        DataSource.UNAUTHORISED ->
            return WrappedResponse(ResponseCode.UNAUTHORISED, ResponseMessage.UNAUTHORISED)
        DataSource.NOT_FOUND ->
            return WrappedResponse(ResponseCode.NOT_FOUND, ResponseMessage.NOT_FOUND)
        DataSource.INTERNAL_SERVER_ERROR ->
            return WrappedResponse(
                ResponseCode.INTERNAL_SERVER_ERROR,
                ResponseMessage.INTERNAL_SERVER_ERROR
            )
        DataSource.CONNECT_TIMEOUT ->
            return WrappedResponse(
                ResponseCode.CONNECT_TIMEOUT, ResponseMessage.CONNECT_TIMEOUT
            )
        DataSource.CANCEL ->
            return WrappedResponse(ResponseCode.CANCEL, ResponseMessage.CANCEL)
        DataSource.RECEIVE_TIMEOUT ->
            return WrappedResponse(
                ResponseCode.RECEIVE_TIMEOUT, ResponseMessage.RECEIVE_TIMEOUT
            )
        DataSource.SEND_TIMEOUT ->
            return WrappedResponse(ResponseCode.SEND_TIMEOUT, ResponseMessage.SEND_TIMEOUT)
        DataSource.CACHE_ERROR ->
            return WrappedResponse(ResponseCode.CACHE_ERROR, ResponseMessage.CACHE_ERROR)
        DataSource.BAD_RESPONSE -> return WrappedResponse(
            ResponseCode.BAD_RESPONSE,
            ResponseMessage.BAD_RESPONSE
        )
        DataSource.NO_INTERNET_CONNECTION ->
            return WrappedResponse(
                ResponseCode.NO_INTERNET_CONNECTION,
                ResponseMessage.NO_INTERNET_CONNECTION
            )
        DataSource.DEFAULT ->
            return WrappedResponse(ResponseCode.DEFAULT, ResponseMessage.DEFAULT)
    }
}

class ResponseCode {
    companion object {
        const val SUCCESS = 200 // success with data
        const val NO_CONTENT = 201 // success with no data (no content)
        const val BAD_REQUEST = 400 // failure, API rejected request
        const val UNAUTHORISED = 401 // failure, user is not authorised
        const val FORBIDDEN = 403 //  failure, API rejected request
        const val INTERNAL_SERVER_ERROR = 500 // failure, crash in server side
        const val NOT_FOUND = 404 // failure, not found

        // local status code
        const val CONNECT_TIMEOUT = -1
        const val CANCEL = -2
        const val RECEIVE_TIMEOUT = -3
        const val SEND_TIMEOUT = -4
        const val CACHE_ERROR = -5
        const val NO_INTERNET_CONNECTION = -7
        const val BAD_RESPONSE = -6
        const val DEFAULT = -8
    }
}

class ResponseMessage {
    companion object {


        const val SUCCESS = "success" // success with data
        const val NO_CONTENT =
            "success" // success with no data (no content)
        const val BAD_REQUEST =
            "Bad request, Try again later" // failure, API rejected request
        const val UNAUTHORISED =
            "User is unauthorised, Try again later" // failure, user is not authorised
        const val FORBIDDEN =
            "Forbidden request, Try again later" //  failure, API rejected request
        const val INTERNAL_SERVER_ERROR =
            "Internal Error occurred, contact support or, Try again later" // failure, crash in server side
        const val NOT_FOUND =
            "Some thing went wrong, Try again later" // failure, crash in server side

        // local status code
        const val CONNECT_TIMEOUT = "Time out error, Try again later"
        const val CANCEL = "Request was cancelled, Try again later"
        const val RECEIVE_TIMEOUT = "Time out error, Try again later"
        const val SEND_TIMEOUT = "Time out error, Try again later"
        const val CACHE_ERROR = "Cache error, Try again later"
        const val BAD_RESPONSE = "Incorrect Response obtained, try again later"
        const val NO_INTERNET_CONNECTION = "Please check your internet connection"
        const val DEFAULT = "Some thing went wrong, Try again later"
    }
}

class ApiInternalStatus {
    companion object {
        const val FAILURE = 0
        const val SUCCESS = 1

    }
}