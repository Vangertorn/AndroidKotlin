package com.example.myapplication.screen.rename

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentRenameBinding
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
        viewBinding.btnRenameUserRename.setOnClickListener {
            viewModel.renameUser(viewBinding.editUserNameRename.text.toString())
            findNavController().navigateSafe(RenameFragmentDirections.actionRenameFragmentToMainFragment())
        }


    }
}