package com.example.myapplication.screen.enter_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentEnterBinding
import org.koin.android.viewmodel.ext.android.viewModel

class EnterFragment : Fragment() {
    private lateinit var viewBinding: FragmentEnterBinding
    private val viewModel: EnterViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentEnterBinding.bind(
            LayoutInflater.from(context).inflate(R.layout.fragment_enter, container, false)
        )
        return viewBinding.root
    }
}