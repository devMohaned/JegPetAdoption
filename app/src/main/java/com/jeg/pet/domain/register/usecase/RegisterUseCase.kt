package com.jeg.pet.domain.register.usecase

import com.jeg.pet.data.common.utils.WrappedResponse
import com.jeg.pet.data.register.remote.dto.RegisterRequest
import com.jeg.pet.data.register.remote.dto.RegisterResponse
import com.jeg.pet.domain.common.BaseResult
import com.jeg.pet.domain.register.model.RegisterUser
import com.jeg.pet.domain.register.repo.RegisterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val registerRepository: RegisterRepository) {
    suspend fun invoke(registerRequest: RegisterRequest) : Flow<BaseResult<WrappedResponse<RegisterResponse>, RegisterUser>> {
        return registerRepository.register(registerRequest)
    }
}