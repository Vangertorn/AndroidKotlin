package com.example.myapplication.screen.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentSettingBinding
import com.example.myapplication.support.navigateSafe
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.runBlocking
import org.koin.android.viewmodel.ext.android.viewModel

class SettingFragment : Fragment() {
    private lateinit var viewBinding: FragmentSettingBinding
    private val viewModel: SettingViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentSettingBinding.bind(
            LayoutInflater.from(context).inflate(R.layout.fragment_setting, container, false)
        )
        return viewBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.btnLogOut.setOnClickListener {
            runBlocking {
                viewModel.logout()
            }
            findNavController().navigateSafe(SettingFragmentDirections.actionSettingFragmentToStartFragment())
        }
        viewBinding.settingToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.userNameLiveDate.observe(this.viewLifecycleOwner) {

            viewBinding.btnDeleteUser.text = getString(R.string.Delete_this_user) + "\t" + "\"$it\""
            viewBinding.btnRenameUser.text = getString(R.string.rename_username) + "\t" + "\"$it\""
        }
        viewModel.userLiveDate.observe(this.viewLifecycleOwner) { user ->
            viewBinding.btnRenameUser.setOnClickListener {
                findNavController().navigateSafe(
                    SettingFragmentDirections.actionSettingFragmentToRenameFragment(
                        user
                    )
                )
            }
        }

        viewBinding.btnDeleteUser.setOnClickListener {
            showDeleteDialog()
        }
        viewModel.progressLiveDate.observe(this.viewLifecycleOwner) {
            if (it) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.User_deleted_successfully),
                    Toast.LENGTH_LONG
                ).show()
                viewBinding.indicatorProgress.isVisible = false
                findNavController().navigateSafe(SettingFragmentDirections.actionSettingFragmentToStartFragment())
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.User_not_delete),
                    Toast.LENGTH_LONG
                ).show()
                viewBinding.indicatorProgress.isVisible = false
            }
        }
    }


    private fun showDeleteDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.Cloud_storage))
            .setMessage(getString(R.string.Agree_at_delete))
            .setPositiveButton(getString(R.string.Yes)) { dialog, _ ->
                viewModel.deleteUser()
                viewBinding.indicatorProgress.isVisible = true
                dialog.cancel()
            }.setNegativeButton(getString(R.string.No)) { dialog, _ ->
                dialog.cancel()
            }.show()

    }
}
