package com.example.myapplication.screen.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentMainBinding

class MainFragment: Fragment() {
    private lateinit var viewBinding: FragmentMainBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentMainBinding.bind(
            LayoutInflater.from(context).inflate(R.layout.fragment_main, container, false)
        )
        return viewBinding.root
    }

}