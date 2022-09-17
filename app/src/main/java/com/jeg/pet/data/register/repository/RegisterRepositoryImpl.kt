package com.jeg.pet.data.register.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jeg.pet.data.common.utils.ErrorHandler
import com.jeg.pet.data.common.utils.WrappedResponse
import com.jeg.pet.data.register.remote.api.RegisterApi
import com.jeg.pet.data.register.remote.dto.RegisterRequest
import com.jeg.pet.data.register.remote.dto.RegisterResponse
import com.jeg.pet.domain.common.BaseResult
import com.jeg.pet.domain.register.model.RegisterUser
import com.jeg.pet.domain.register.repo.RegisterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import java.lang.NullPointerException
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor(private val registerApi: RegisterApi) :
    RegisterRepository {
    override suspend fun register(registerRequest: RegisterRequest): Flow<BaseResult<WrappedResponse<RegisterResponse>, RegisterUser>> {
        return flow {
            try {
                val response = registerApi.register(registerRequest)
                if (response.isSuccessful) {
                    val body = response.body()!!
                    val id = body.data?.id
                    val name = body.data?.name
                    val email = body.data?.email
                    val token = body.data?.token
                    if (id == null || name == null || email == null || token == null) throw NullPointerException()
                    val registerUser = RegisterUser(id, name, email, token)
                    emit(BaseResult.Success(registerUser))
                } else {
                    val errorBody: ResponseBody =
                        response.errorBody() ?: throw NullPointerException()
                    val type = object : TypeToken<WrappedResponse<RegisterResponse>>() {}.type
                    val err: WrappedResponse<RegisterResponse> =
                        Gson().fromJson(errorBody.charStream(), type)
                    emit(BaseResult.Error(err))
                }
            } catch (exception: Exception) {
                val err = ErrorHandler<RegisterResponse>().handleException(exception)
                emit(BaseResult.Error(err))
            }
        }
    }
}