package com.example.myapplication.screen.sign_up

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentSingUpBinding
import com.example.myapplication.repository.LoginResult
import com.example.myapplication.support.SupportFragmentInset
import com.example.myapplication.support.hideKeyboard
import com.example.myapplication.support.navigateSafe
import com.example.myapplication.support.setVerticalMargin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel


class SingUpFragment : SupportFragmentInset<FragmentSingUpBinding>(R.layout.fragment_sing_up) {
    override val viewBinding: FragmentSingUpBinding by viewBinding()
    private val viewModel: SingUpViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.createNewUserResultLiveData.observe(this.viewLifecycleOwner) { loginResult ->
            when (loginResult) {

                LoginResult.PASSWORDS_DO_NOT_MATCH -> showToast(loginResult)

                LoginResult.NONE -> Unit

                LoginResult.EMPTY_FIELDS -> showToast(loginResult)

                LoginResult.USER_CREATED_SUCCESSFUL -> showToast(loginResult)

                LoginResult.USER_ALREADY_EXISTS -> showToast(loginResult)

                else -> Toast.makeText(
                    requireContext(),
                    "It was happen something terrible",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        viewModel.logGedIn.observe(this.viewLifecycleOwner) { loggedIn ->
            if (loggedIn) {
                GlobalScope.launch(Dispatchers.Main) {
                    delay(50)
                    findNavController().navigateSafe(SingUpFragmentDirections.actionSingUpFragmentToMainFragment())
                    hideKeyboard()
                }

            } else {
                viewBinding.root.alpha = 1f
            }
        }
        viewBinding.btnRegisterNewUserSignUp.setOnClickListener {
            viewModel.createNewUser(
                viewBinding.editUserNameSignUp.text.toString(),
                viewBinding.editPasswordSingUp.text.toString(),
                viewBinding.repeatEditPasswordSingUp.text.toString()
            )
        }
        viewBinding.btnBackSignUp.setOnClickListener {
            findNavController().popBackStack()
        }
        viewBinding.editUserNameSignUp.doOnTextChanged { text, start, before, count ->
            if (text!!.length in 1..2) {
                viewBinding.editUserNameInputLayout.error =
                    getString(R.string.the_name_is_too_short)
            } else {
                viewBinding.editUserNameInputLayout.error = null
            }
        }
    }

    private fun showToast(loginResult: LoginResult) {
        Toast.makeText(
            requireContext(),
            loginResult.toast,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onResume() {
        super.onResume()
        viewBinding.passwordInputLayoutSingUp.setHint(R.string.password)
        viewBinding.repeatTextInputLayoutSingUp.setHint(R.string.repeat_password)
        viewBinding.editUserNameInputLayout.setHint(R.string.enter_your_username_please)
    }

    override fun onInsetsReceived(top: Int, bottom: Int, hasKeyboard: Boolean) {
        viewBinding.btnBackSignUp.setVerticalMargin(20, bottom)
    }
}