package com.example.myapplication.screen.enter

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentLoginBinding

import com.example.myapplication.repository.LoginResult
import com.example.myapplication.support.SupportFragmentInset
import com.example.myapplication.support.hideKeyboard
import com.example.myapplication.support.navigateSafe
import com.example.myapplication.support.setVerticalMargin
import org.koin.android.viewmodel.ext.android.viewModel

class LoginFragment : SupportFragmentInset<FragmentLoginBinding>(R.layout.fragment_login) {
    override val viewBinding: FragmentLoginBinding by viewBinding()
    private val viewModel: LoginViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loginResultLiveData.observe(this.viewLifecycleOwner) { loginResult ->
            when (loginResult) {

                LoginResult.USER_NOT_EXIST -> showToast(loginResult)

                LoginResult.WRONG_PASSWORD -> showToast(loginResult)

                LoginResult.NONE -> Unit

                LoginResult.EMPTY_FIELDS -> showToast(loginResult)

                LoginResult.LOGIN_COMPLETED_SUCCESSFULLY -> showToast(loginResult)

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
                findNavController().navigateSafe(LoginFragmentDirections.actionLoginFragmentToMainFragment())
                hideKeyboard()
            } else {
                viewBinding.root.alpha = 1f
            }
        }


        viewBinding.btnLogin.setOnClickListener {
            viewModel.login(
                viewBinding.editUserName.text.toString(),
                viewBinding.editPassword.text.toString()
            )
        }
        viewBinding.btnRegisterNewUser.setOnClickListener {
            findNavController().navigateSafe(LoginFragmentDirections.actionLoginFragmentToSingUpFragment())
            hideKeyboard()
        }
        viewModel.autoCompleteUserNamesLiveData.observe(this.viewLifecycleOwner) { userNames ->
            val arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                requireContext(),
                R.layout.select_dialog_item,
                userNames
            )
            viewBinding.editUserName.setAdapter(arrayAdapter)
        }
        viewBinding.editUserName.setDropDownBackgroundResource(android.R.color.transparent)
    }

    override fun onResume() {
        super.onResume()
        viewBinding.editPassword1.setHint(R.string.password)
        viewBinding.editUserNameInputLayout.setHint(R.string.enter_your_username_please)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.loginResultLiveData.postValue(LoginResult.NONE)
    }

    private fun showToast(loginResult: LoginResult) {
        Toast.makeText(
            requireContext(),
            loginResult.toast,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onInsetsReceived(top: Int, bottom: Int, hasKeyboard: Boolean) {
        viewBinding.btnRegisterNewUser.setVerticalMargin(0, bottom)
    }


}