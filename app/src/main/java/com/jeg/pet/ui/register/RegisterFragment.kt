package com.jeg.pet.ui.register

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.jeg.pet.R
import com.jeg.pet.app.util.isEmail
import com.jeg.pet.data.common.utils.SharedPrefs
import com.jeg.pet.data.common.utils.WrappedResponse
import com.jeg.pet.data.login.sources.remote.dto.LoginResponse
import com.jeg.pet.data.register.remote.dto.RegisterRequest
import com.jeg.pet.data.register.remote.dto.RegisterResponse
import com.jeg.pet.databinding.FragmentRegisterBinding
import com.jeg.pet.domain.login.model.LoginUser
import com.jeg.pet.domain.register.model.RegisterUser
import com.jeg.pet.ui.common.BaseFragment
import com.jeg.pet.ui.common.showGenericAlertDialog
import com.jeg.pet.ui.common.showToast
import com.jeg.pet.ui.home.HomeActivity
import com.jeg.pet.ui.login.LoginActivityState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>(){


    private val viewModel: RegisterViewModel by viewModels()
    @Inject
    lateinit var sharedPrefs: SharedPrefs
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backToLoginButton.setOnClickListener() {
            findNavController().navigateUp()
        }

        binding.registerButton.setOnClickListener{
            val fullName = binding.fullNameTextInput.editText?.text.toString().trim()
            val email = binding.emailTextInput.editText?.text.toString().trim()
            val password = binding.passwordTextInput.editText?.text.toString().trim()
            if(validate(fullName,email, password)){
                val registerRequest = RegisterRequest(fullName ,email, password)
                viewModel.register(registerRequest)
            }
        }
        observe()

    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRegisterBinding = FragmentRegisterBinding.inflate(inflater,container,false)


    private fun validate(fullName: String,email: String, password: String) : Boolean{
        resetAllInputError()
        if(fullName.length <= 4 || fullName.trim().isEmpty())
        {
            setNameError(getString(R.string.name_is_too_short))
            return false
        }
        if(fullName.contains("  "))
        {
            setNameError(getString(R.string.double_spaces))
            return false
        }


        if(!email.isEmail()){
            setEmailError(getString(R.string.error_email_not_valid))
            return false
        }

        if(password.length < 7 && password.length < 256){
            setPasswordError(getString(R.string.error_password_not_valid))
            return false
        }

        return true
    }

    private fun resetAllInputError(){
        setNameError(null)
        setEmailError(null)
        setPasswordError(null)
    }

    private fun setNameError(e : String?){
        binding.fullNameTextInput.error = e
    }


    private fun setEmailError(e : String?){
        binding.emailTextInput.error = e
    }

    private fun setPasswordError(e: String?){
        binding.passwordTextInput.error = e
    }

    private fun observe(){
        lifecycleScope.launchWhenStarted {
            viewModel.state.collectLatest {
                    state -> handleStateChange(state)
            }
        }

    }

    private fun handleStateChange(state: RegisterActivityState){
        when(state){
            is RegisterActivityState.Init -> Unit
            is RegisterActivityState.ErrorRegister -> handleErrorRegister(state.rawResponse)
            is RegisterActivityState.SuccessRegister -> handleSuccessRegister(state.registerUser)
            is RegisterActivityState.ShowToast -> context?.showToast(state.message)
            is RegisterActivityState.IsLoading -> handleLoading(state.isLoading)
        }
    }

    private fun handleSuccessRegister(registerUser: RegisterUser){
        sharedPrefs.saveToken(registerUser.token)
      //  setResult(AppCompatActivity.RESULT_OK)
        navigateToHome()
    }

    private fun handleErrorRegister(httpResponse: WrappedResponse<RegisterResponse>){
        context?.showGenericAlertDialog(httpResponse.message)
    }

    private fun handleLoading(isLoading: Boolean){
        binding.registerButton.isEnabled = !isLoading
        binding.loadingProgressBar.isIndeterminate = isLoading
        binding.loadingProgressBar.visibility = View.VISIBLE
        if(!isLoading){
            binding.loadingProgressBar.progress = 0
            binding.loadingProgressBar.visibility = View.GONE
        }
    }

    private fun navigateToHome(){
        context?.showToast("You're now registered")
        var intent: Intent = Intent(context, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context?.startActivity(intent)
    }

}