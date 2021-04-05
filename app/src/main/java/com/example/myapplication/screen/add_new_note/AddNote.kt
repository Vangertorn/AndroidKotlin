package com.example.myapplication.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentAddNoteBinding

class addNote : Fragment() {

    private lateinit var viewBinding: FragmentAddNoteBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentAddNoteBinding.bind(
            LayoutInflater.from(context).inflate(R.layout.fragment_add_note, container, false)
        )
        viewBinding.button1.setOnClickListener { this }
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


    }

}