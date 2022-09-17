package com.jeg.pet.domain.login.repository

import com.jeg.pet.data.common.utils.WrappedResponse
import com.jeg.pet.data.login.sources.remote.dto.LoginRequest
import com.jeg.pet.data.login.sources.remote.dto.LoginResponse
import com.jeg.pet.domain.common.BaseResult
import com.jeg.pet.domain.login.model.LoginUser
import kotlinx.coroutines.flow.Flow


interface LoginRepository {

    suspend fun login(loginRequest: LoginRequest) : Flow<BaseResult<WrappedResponse<LoginResponse>,LoginUser>>

//    suspend fun login(email: String, password: String) : Resource<LoginResponse>
//    suspend fun register(fullName: String, email: String, password: String) : Resource<UserDto>

}