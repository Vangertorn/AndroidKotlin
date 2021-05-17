package com.example.myapplication.screen.enter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentLoginBinding

import com.example.myapplication.repository.LoginResult
import com.example.myapplication.support.navigateSafe
import org.koin.android.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {
    private lateinit var viewBinding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentLoginBinding.bind(
            LayoutInflater.from(context).inflate(R.layout.fragment_login, container, false)
        )
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loginResultLiveData.observe(this.viewLifecycleOwner) { loginResult ->
            when (loginResult) {
                LoginResult.USER_NOT_EXIST -> Toast.makeText(
                    requireContext(),
                    loginResult.toast,
                    Toast.LENGTH_SHORT
                ).show()
                LoginResult.WRONG_PASSWORD -> Toast.makeText(
                    requireContext(),
                    loginResult.toast,
                    Toast.LENGTH_SHORT
                ).show()
                LoginResult.NONE -> Unit
                LoginResult.EMPTY_FIELDS -> Toast.makeText(
                    requireContext(),
                    loginResult.toast,
                    Toast.LENGTH_SHORT
                ).show()
                LoginResult.LOGIN_COMPLETED_SUCCESSFULLY -> Toast.makeText(
                    requireContext(),
                    loginResult.toast,
                    Toast.LENGTH_SHORT
                ).show()
                LoginResult.USER_CREATED_SUCCESSFUL -> Toast.makeText(
                    requireContext(),
                    loginResult.toast,
                    Toast.LENGTH_SHORT
                ).show()
                LoginResult.USER_ALREADY_EXISTS -> Toast.makeText(
                    requireContext(),
                    loginResult.toast,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        viewModel.logGedIn.observe(this.viewLifecycleOwner) { loggedIn ->
            if (loggedIn) {
                findNavController().navigateSafe(LoginFragmentDirections.actionLoginFragmentToMainFragment())
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
        }
        viewModel.autoCompleteUserNamesLiveData.observe(this.viewLifecycleOwner) { userNames ->
            val arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                requireContext(),
                R.layout.select_dialog_item,
                userNames
            )
            viewBinding.editUserName.setAdapter(arrayAdapter)
        }
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


}