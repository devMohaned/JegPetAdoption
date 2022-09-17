package com.jeg.pet.data.login.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jeg.pet.data.common.utils.ErrorHandler
import com.jeg.pet.data.common.utils.WrappedResponse
import com.jeg.pet.domain.login.repository.LoginRepository
import com.jeg.pet.data.login.sources.remote.api.LoginApi
import com.jeg.pet.data.login.sources.remote.dto.LoginRequest
import com.jeg.pet.data.login.sources.remote.dto.LoginResponse
import com.jeg.pet.domain.common.BaseResult
import com.jeg.pet.domain.login.model.LoginUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import java.lang.NullPointerException
import javax.inject.Inject


class LoginRepositoryImpl @Inject constructor(private val loginApi: LoginApi) : LoginRepository {
    override suspend fun login(loginRequest: LoginRequest):
            Flow<BaseResult<WrappedResponse<LoginResponse>, LoginUser>> {
        return flow {
            try {
                val response = loginApi.login(loginRequest)
                if (response.isSuccessful) {
                    val body = response.body()!!
                    val id = body.data?.id
                    val name = body.data?.name
                    val email = body.data?.email
                    val token = body.data?.token
                    if (id == null || name == null || email == null || token == null) throw NullPointerException()
                    val loginUser = LoginUser(id, name, email, token)
                    emit(BaseResult.Success(loginUser))

                } else {
                    val errorBody: ResponseBody =
                        response.errorBody() ?: throw NullPointerException()
                    val type = object : TypeToken<WrappedResponse<LoginResponse>>() {}.type
                    val err: WrappedResponse<LoginResponse> =
                        Gson().fromJson(errorBody.charStream(), type)
                    emit(BaseResult.Error(err))

                }
            } catch (exception: Exception) {
                val err = ErrorHandler<LoginResponse>().handleException(exception)
                emit(BaseResult.Error(err))
            }
        }
    }
}