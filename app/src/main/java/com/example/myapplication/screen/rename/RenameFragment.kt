package com.example.myapplication.screen.rename

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentRenameBinding
import com.example.myapplication.repository.LoginResult
import com.example.myapplication.support.navigateSafe
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class RenameFragment : Fragment() {
    private lateinit var viewBinding: FragmentRenameBinding
    private val viewModel: RenameViewModel by viewModel()
    private val args: RenameFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentRenameBinding.bind(
            LayoutInflater.from(context).inflate(R.layout.fragment_rename, container, false)
        )
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args.user?.let {
            viewBinding.editUserNameRename.setText(it.name)
        }
        viewModel.renameResultLiveData.observe(this.viewLifecycleOwner) { loginResult ->
            when (loginResult) {
                LoginResult.EMPTY_FIELDS -> Toast.makeText(
                    requireContext(),
                    loginResult.toast,
                    Toast.LENGTH_SHORT
                ).show()

                LoginResult.USER_RENAME_SUCCESSFUL -> {
                    Toast.makeText(
                        requireContext(),
                        loginResult.toast,
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigateSafe(RenameFragmentDirections.actionRenameFragmentToMainFragment())
                }

                LoginResult.USER_ALREADY_EXISTS -> Toast.makeText(
                    requireContext(),
                    loginResult.toast,
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


    }
}