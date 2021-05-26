package com.example.myapplication.screen.setting

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentSettingBinding
import com.example.myapplication.support.SupportFragmentInset
import com.example.myapplication.support.navigateSafe
import com.example.myapplication.support.setVerticalMargin
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.koin.android.viewmodel.ext.android.viewModel

class SettingFragment : SupportFragmentInset<FragmentSettingBinding>(R.layout.fragment_setting) {
    override val viewBinding: FragmentSettingBinding by viewBinding()
    private val viewModel: SettingViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.btnLogOut.setOnClickListener {
            runBlocking {
                viewModel.logout()
                delay(50)
                findNavController().navigateSafe(SettingFragmentDirections.actionSettingFragmentToStartFragment())
            }
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

    override fun onInsetsReceived(top: Int, bottom: Int, hasKeyboard: Boolean) {
        viewBinding.settingToolbar.setVerticalMargin(top)
    }
}
