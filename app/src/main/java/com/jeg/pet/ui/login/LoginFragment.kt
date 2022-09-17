package com.jeg.pet.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.jeg.pet.R
import com.jeg.pet.ui.common.BaseFragment
import com.jeg.pet.app.util.isEmail
import com.jeg.pet.data.common.utils.SharedPrefs
import com.jeg.pet.data.common.utils.WrappedResponse
import com.jeg.pet.databinding.FragmentLoginBinding
import com.jeg.pet.data.login.sources.remote.dto.LoginRequest
import com.jeg.pet.data.login.sources.remote.dto.LoginResponse
import com.jeg.pet.domain.login.model.LoginUser
import com.jeg.pet.ui.common.showGenericAlertDialog
import com.jeg.pet.ui.common.showToast
import com.jeg.pet.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private val viewModel: LoginViewModel by viewModels()
    @Inject
    lateinit var sharedPrefs: SharedPrefs

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.createNewAccountButton.setOnClickListener() {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.loginButton.setOnClickListener{
            val email = binding.emailTextInput.editText?.text.toString().trim()
            val password = binding.passwordTextInput.editText?.text.toString().trim()
            if(validate(email, password)){
                val loginRequest = LoginRequest(email, password)
                viewModel.login(loginRequest)
            }
        }

        binding.continueAnonymousButton.setOnClickListener {
            navigateToHome()
        }

        observe()

    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLoginBinding = FragmentLoginBinding.inflate(inflater,container,false)


    private fun validate(email: String, password: String) : Boolean{
        resetAllInputError()
        if(!email.isEmail()){
            setEmailError(getString(R.string.error_email_not_valid))
            return false
        }

        if(password.length < 8){
            setPasswordError(getString(R.string.error_password_not_valid))
            return false
        }

        return true
    }

    private fun resetAllInputError(){
        setEmailError(null)
        setPasswordError(null)
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

    private fun handleStateChange(state: LoginActivityState){
        when(state){
            is LoginActivityState.Init -> Unit
            is LoginActivityState.ErrorLogin -> handleErrorLogin(state.rawResponse)
            is LoginActivityState.SuccessLogin -> handleSuccessLogin(state.loginUser)
            is LoginActivityState.ShowToast -> context?.showToast(state.message)
            is LoginActivityState.IsLoading -> handleLoading(state.isLoading)
        }
    }

    private fun handleErrorLogin(response: WrappedResponse<LoginResponse>){
        context?.showGenericAlertDialog(response.message)
    }

    private fun handleLoading(isLoading: Boolean){
        binding.loginButton.isEnabled = !isLoading
        binding.createNewAccountButton.isEnabled = !isLoading
        binding.loadingProgressBar.isIndeterminate = isLoading
        binding.loadingProgressBar.visibility = VISIBLE
        if(!isLoading){
            binding.loadingProgressBar.progress = 0
            binding.loadingProgressBar.visibility = GONE
        }
    }

    private fun handleSuccessLogin(loginUser: LoginUser){
        sharedPrefs.saveToken(loginUser.token)
        context?.showToast("Welcome ${loginUser.fullName}")
        navigateToHome()
    }

    private fun navigateToHome(){
        var intent: Intent = Intent(context, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context?.startActivity(intent)
 //       findNavController().navigate(R.id.action_loginFragment_to_resetPasswordFragment)
    }

}