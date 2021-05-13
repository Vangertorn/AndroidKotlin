package com.example.myapplication.screen.sign_up

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentSingUpBinding
import com.example.myapplication.repository.LoginResult
import com.example.myapplication.screen.enter.LoginFragmentDirections
import com.example.myapplication.support.navigateSafe
import org.koin.android.viewmodel.ext.android.viewModel


class SingUpFragment: Fragment() {
    private lateinit var viewBinding: FragmentSingUpBinding
    private val viewModel: SingUpViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentSingUpBinding.bind(LayoutInflater.from(context).inflate(R.layout.fragment_sing_up,container,false))
        return viewBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.logGedIn.observe(this.viewLifecycleOwner) { loggedIn ->
            if (loggedIn) {
                findNavController().navigateSafe(SingUpFragmentDirections.actionSingUpFragmentToMainFragment())
            } else {
                viewBinding.root.alpha = 1f
            }
        }
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
            }
        }
        viewBinding.btnRegisterNewUserSignUp.setOnClickListener {
            viewModel.createNewUser(
                viewBinding.editUserNameSignUp.text.toString(),
                viewBinding.editPasswordSingUp.text.toString(),
                viewBinding.repeatEditPasswordSingUp.text.toString()
            )
        }
    }
}