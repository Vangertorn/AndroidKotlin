package com.example.myapplication.screen.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentSettingBinding
import com.example.myapplication.support.navigateSafe
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
            viewModel.logout()
            findNavController().navigateSafe(SettingFragmentDirections.actionSettingFragmentToStartFragment())
        }
        viewBinding.settingToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.userNameLiveDate.observe(this.viewLifecycleOwner) {

            viewBinding.btnDeleteUser.text = "Delete user \"$it\""
        }

        viewBinding.btnDeleteUser.setOnClickListener {
            showDeleteDialog()
        }
    }

    private fun showDeleteDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Cloud storage")
            .setMessage("Are you sure? All user data will be removed from device!")
            .setPositiveButton("Yes") { dialog, _ ->
                viewModel.deleteUser()
                dialog.cancel()
                findNavController().navigateSafe(SettingFragmentDirections.actionSettingFragmentToStartFragment())
            }.setNegativeButton("No") { dialog, _ ->
                dialog.cancel()
            }.show()

    }
}
