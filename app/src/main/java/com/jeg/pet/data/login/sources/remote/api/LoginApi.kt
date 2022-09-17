package com.jeg.pet.data.login.sources.remote.api

import com.jeg.pet.data.common.utils.WrappedResponse
import com.jeg.pet.data.login.sources.remote.dto.LoginRequest
import com.jeg.pet.data.login.sources.remote.dto.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginApi {
    @POST("auth/loginHandler.php")
    suspend fun login(@Body loginRequest: LoginRequest)
    : Response<WrappedResponse<LoginResponse>>

}