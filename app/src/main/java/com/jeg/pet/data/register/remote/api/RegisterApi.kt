package com.jeg.pet.data.register.remote.api

import com.jeg.pet.data.common.utils.WrappedResponse
import com.jeg.pet.data.register.remote.dto.RegisterRequest
import com.jeg.pet.data.register.remote.dto.RegisterResponse

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterApi {
    @POST("auth/registerHandler.php")
    suspend fun register(@Body registerRequest: RegisterRequest) : Response<WrappedResponse<RegisterResponse>>
}