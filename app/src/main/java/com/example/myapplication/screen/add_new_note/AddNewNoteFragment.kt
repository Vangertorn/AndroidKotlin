package com.example.myapplication.screen.add_new_note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentAddNewNoteBinding
import java.util.*

class AddNewNoteFragment : Fragment() {
    private lateinit var viewBinding: FragmentAddNewNoteBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentAddNewNoteBinding.bind(
            LayoutInflater.from(context).inflate(R.layout.fragment_add_new_note, container, false)
        )
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        viewBinding.create.setOnClickListener {
            if (viewBinding.textNote.text.isNotBlank()) {
                setFragmentResult(ADD_NEW_RESULT, Bundle().apply {
                    putString(TEXT, viewBinding.textNote.text.toString())
                    putString(DATE, viewBinding.dateNote.text.toString())
                })

                findNavController().popBackStack()

            } else {
                Toast.makeText(requireContext(), "Could You enter note, please",Toast.LENGTH_LONG)
                    .show()
            }

        }
    }

    companion object {
        const val ADD_NEW_RESULT = "ADD_NEW_RESULT"
        const val TEXT = "TEXT"
        const val DATE = "DATE"
    }

}