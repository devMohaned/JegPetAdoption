package com.jeg.pet.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeg.pet.data.common.utils.WrappedResponse
import com.jeg.pet.data.register.remote.dto.RegisterRequest
import com.jeg.pet.data.register.remote.dto.RegisterResponse
import com.jeg.pet.domain.common.BaseResult
import com.jeg.pet.domain.register.model.RegisterUser
import com.jeg.pet.domain.register.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(private val registerUseCase: RegisterUseCase) : ViewModel() {
    private val _state = MutableStateFlow<RegisterActivityState>(RegisterActivityState.Init)
    val state: StateFlow<RegisterActivityState> get() = _state

    private fun setLoading(){
        _state.value = RegisterActivityState.IsLoading(true)
    }

    private fun hideLoading(){
        _state.value = RegisterActivityState.IsLoading(false)
    }

    private fun showToast(message: String){
        _state.value  = RegisterActivityState.ShowToast(message)
    }

    private fun successRegister(registerUser: RegisterUser){
        _state.value = RegisterActivityState.SuccessRegister(registerUser)
    }

    private fun failedRegister(rawResponse: WrappedResponse<RegisterResponse>){
        _state.value = RegisterActivityState.ErrorRegister(rawResponse)
    }

    fun register(registerRequest: RegisterRequest){
        viewModelScope.launch {
            registerUseCase.invoke(registerRequest)
                .onStart {
                    setLoading()
                }
                .catch { exception ->
                    showToast(exception.message.toString())
                    hideLoading()
                }
                .collect { result ->
                    hideLoading()
                    when(result){
                        is BaseResult.Success -> successRegister(result.data)
                        is BaseResult.Error -> failedRegister(result.rawResponse)
                        is BaseResult.Loading -> setLoading()
                    }
                }
        }
    }
}

sealed class RegisterActivityState {
    object Init : RegisterActivityState()
    data class IsLoading(val isLoading: Boolean) : RegisterActivityState()
    data class ShowToast(val message: String) : RegisterActivityState()
    data class SuccessRegister(val registerUser: RegisterUser) : RegisterActivityState()
    data class ErrorRegister(val rawResponse: WrappedResponse<RegisterResponse>) : RegisterActivityState()
}