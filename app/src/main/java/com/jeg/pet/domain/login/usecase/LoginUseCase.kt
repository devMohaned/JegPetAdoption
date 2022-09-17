package com.jeg.pet.domain.login.usecase

import com.jeg.pet.data.common.utils.WrappedResponse
import com.jeg.pet.data.login.sources.remote.dto.LoginRequest
import com.jeg.pet.data.login.sources.remote.dto.LoginResponse
import com.jeg.pet.domain.common.BaseResult
import com.jeg.pet.domain.login.model.LoginUser
import com.jeg.pet.domain.login.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class LoginUseCase @Inject constructor(private val loginRepository: LoginRepository) {
    suspend fun execute(loginRequest: LoginRequest): Flow<BaseResult<WrappedResponse<LoginResponse>,LoginUser>> {
        return loginRepository.login(loginRequest)
    }

}