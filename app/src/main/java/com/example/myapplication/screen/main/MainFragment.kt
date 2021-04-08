package com.example.myapplication.screen.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentMainBinding
import com.example.myapplication.screen.add_new_note.AddNewNoteFragment
import com.example.myapplication.support.navigateSafe

class MainFragment : Fragment() {
    private lateinit var viewBinding: FragmentMainBinding
    private lateinit var viewModel: MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = MainViewModel()
        viewBinding = FragmentMainBinding.bind(
            LayoutInflater.from(context).inflate(R.layout.fragment_main, container, false)
        )
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.listLiveData.observe(this.viewLifecycleOwner, {
            viewBinding.recyclerView.adapter = NotesRecyclerViewAdapter(it, { position -> })
        })
        setFragmentResultListener(AddNewNoteFragment.ADD_NEW_RESULT) { key, bundle ->
            val note = bundle.getString(AddNewNoteFragment.TEXT)
            val date = bundle.getString(AddNewNoteFragment.DATE)
            note?.let {
                viewModel.addNotes(it, date)
            }
        }
        viewBinding.recyclerView.scrollToPosition(0)


        viewBinding.btnAdd.setOnClickListener {
            findNavController().navigateSafe(MainFragmentDirections.actionMainFragmentToAddNewNoteFragment())
        }

    }

}