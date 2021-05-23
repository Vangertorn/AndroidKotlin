package com.example.myapplication.screen.sign_up

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentSingUpBinding
import com.example.myapplication.repository.LoginResult
import com.example.myapplication.support.hideKeyboard
import com.example.myapplication.support.navigateSafe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel


class SingUpFragment : Fragment() {
    private lateinit var viewBinding: FragmentSingUpBinding
    private val viewModel: SingUpViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentSingUpBinding.bind(
            LayoutInflater.from(context).inflate(R.layout.fragment_sing_up, container, false)
        )
        return viewBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.createNewUserResultLiveData.observe(this.viewLifecycleOwner) { loginResult ->
            when (loginResult) {
                LoginResult.PASSWORDS_DO_NOT_MATCH -> Toast.makeText(
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

    override fun onResume() {
        super.onResume()
        viewBinding.passwordInputLayoutSingUp.setHint(R.string.password)
        viewBinding.repeatTextInputLayoutSingUp.setHint(R.string.repeat_password)
        viewBinding.editUserNameInputLayout.setHint(R.string.enter_your_username_please)
    }
}