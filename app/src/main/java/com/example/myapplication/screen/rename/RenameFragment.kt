package com.example.myapplication.screen.rename

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentRenameBinding
import com.example.myapplication.repository.LoginResult
import com.example.myapplication.support.SupportFragmentInset
import com.example.myapplication.support.navigateSafe
import com.example.myapplication.support.setVerticalMargin
import org.koin.android.viewmodel.ext.android.viewModel

class RenameFragment : SupportFragmentInset<FragmentRenameBinding>(R.layout.fragment_rename) {
    override val viewBinding: FragmentRenameBinding by viewBinding()
    private val viewModel: RenameViewModel by viewModel()
    private val args: RenameFragmentArgs by navArgs()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args.user?.let {
            viewBinding.editUserNameRename.setText(it.name)
        }
        viewModel.renameResultLiveData.observe(this.viewLifecycleOwner) { loginResult ->
            when (loginResult) {

                LoginResult.EMPTY_FIELDS -> showToast(loginResult)

                LoginResult.USER_RENAME_SUCCESSFUL -> {

                    showToast(loginResult)

                    findNavController().navigateSafe(RenameFragmentDirections.actionRenameFragmentToMainFragment())
                }

                LoginResult.USER_ALREADY_EXISTS -> showToast(loginResult)
                else -> Toast.makeText(
                    requireContext(),
                    "It was happen something terrible",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        viewBinding.btnRenameUserRename.setOnClickListener {
            viewModel.renameUser(viewBinding.editUserNameRename.text.toString())
        }
        viewBinding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        viewBinding.editUserNameRename.doOnTextChanged { text, start, before, count ->
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

    override fun onInsetsReceived(top: Int, bottom: Int, hasKeyboard: Boolean) {
        viewBinding.btnBack.setVerticalMargin(0, bottom)
    }
}