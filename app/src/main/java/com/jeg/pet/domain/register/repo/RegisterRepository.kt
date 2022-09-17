package com.jeg.pet.domain.register.repo

import com.jeg.pet.data.common.utils.WrappedResponse
import com.jeg.pet.data.login.sources.remote.dto.LoginRequest
import com.jeg.pet.data.login.sources.remote.dto.LoginResponse
import com.jeg.pet.data.register.remote.dto.RegisterRequest
import com.jeg.pet.data.register.remote.dto.RegisterResponse
import com.jeg.pet.domain.common.BaseResult
import com.jeg.pet.domain.login.model.LoginUser
import com.jeg.pet.domain.register.model.RegisterUser
import kotlinx.coroutines.flow.Flow


interface RegisterRepository {

    suspend fun register(registerRequest: RegisterRequest)
    : Flow<BaseResult<WrappedResponse<RegisterResponse>, RegisterUser>>



}