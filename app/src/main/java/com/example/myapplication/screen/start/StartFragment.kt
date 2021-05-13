package com.example.myapplication.screen.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentStartBinding
import com.example.myapplication.support.navigateSafe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.viewmodel.ext.android.viewModel

class StartFragment : Fragment() {
    private lateinit var viewBinding: FragmentStartBinding

    @ExperimentalCoroutinesApi
    private val viewModel: StartViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentStartBinding.bind(
            LayoutInflater.from(context).inflate(R.layout.fragment_start, container, false)
        )
        return viewBinding.root
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.userNameLiveDate.observe(this.viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                viewBinding.startName.text = it
                viewBinding.root.postDelayed(
                    { findNavController().navigateSafe(StartFragmentDirections.actionStartFragmentToMainFragment()) },
                    1000
                )
            } else {
                viewBinding.root.postDelayed(
                    { findNavController().navigateSafe(StartFragmentDirections.actionStartFragmentToLoginFragment()) },
                    1000
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewBinding.root.removeCallbacks {
            findNavController().navigateSafe(StartFragmentDirections.actionStartFragmentToLoginFragment())
        }
        viewBinding.root.removeCallbacks {
            findNavController().navigateSafe(StartFragmentDirections.actionStartFragmentToMainFragment())
        }

    }

}