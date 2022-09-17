package com.jeg.pet.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeg.pet.data.common.utils.WrappedResponse
import com.jeg.pet.data.login.sources.remote.dto.LoginRequest
import com.jeg.pet.data.login.sources.remote.dto.LoginResponse
import com.jeg.pet.domain.common.BaseResult
import com.jeg.pet.domain.login.model.LoginUser
import com.jeg.pet.domain.login.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase) : ViewModel() {
    private val _state = MutableSharedFlow<LoginActivityState>()
    val state: SharedFlow<LoginActivityState> get() = _state

    private suspend fun setLoading() {
        _state.emit(LoginActivityState.IsLoading(true))
    }

    private suspend fun hideLoading() {
        _state.emit(LoginActivityState.IsLoading(false))
    }

    private suspend fun showToast(message: String) {
        _state.emit(LoginActivityState.ShowToast(message))
    }


    fun login(loginRequest: LoginRequest) {
        viewModelScope.launch {
            loginUseCase.execute(loginRequest)
                .onStart {
                    setLoading()
                }
                .catch { exception ->
                    hideLoading()
                    showToast(exception.message.toString())
                }
                .collect { baseResult ->
                    hideLoading()
                    when (baseResult) {
                        is BaseResult.Error -> _state.emit(LoginActivityState.ErrorLogin(baseResult.rawResponse))
                        is BaseResult.Success -> _state.emit(
                            LoginActivityState.SuccessLogin(
                                baseResult.data
                            )
                        )
                        is BaseResult.Loading -> _state.emit(LoginActivityState.IsLoading(true))
                    }
                }
        }
    }


}


sealed class LoginActivityState {
    object Init : LoginActivityState()
    data class IsLoading(val isLoading: Boolean) : LoginActivityState()
    data class ShowToast(val message: String) : LoginActivityState()
    data class SuccessLogin(val loginUser: LoginUser) : LoginActivityState()
    data class ErrorLogin(val rawResponse: WrappedResponse<LoginResponse>) : LoginActivityState()
}