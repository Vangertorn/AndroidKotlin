package com.example.myapplication.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentAddNoteBinding

class addNote : Fragment(), View.OnClickListener {

    private lateinit var viewBinding: FragmentAddNoteBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentAddNoteBinding.bind(
            LayoutInflater.from(context).inflate(R.layout.fragment_add_note, container, false)
        )
        viewBinding.button1.setOnClickListener { this }
        return viewBinding.root
    }

    fun translateIdToIndex(id: Int): Int {
        var index = -1
        when (id) {
            viewBinding.button1.id -> index = 1
        }
        return index
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


    }

    override fun onClick(v: View?) {
        val buttonIndex = translateIdToIndex(v!!.id)
    }

}