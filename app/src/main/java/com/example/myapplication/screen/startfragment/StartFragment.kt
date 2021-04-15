package com.example.myapplication.screen.startfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentStartBinding
import com.example.myapplication.support.navigateSafe

class StartFragment : Fragment() {
    private lateinit var viewBinding: FragmentStartBinding

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.root.postDelayed(
            { findNavController().navigateSafe(StartFragmentDirections.actionStartFragmentToMainFragment()) },
            1000
        )
    }

}