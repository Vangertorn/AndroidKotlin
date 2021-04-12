package com.example.myapplication.screen.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentMainBinding
import com.example.myapplication.screen.note_details.NoteDetailsFragment
import com.example.myapplication.support.navigateSafe


class MainFragment : Fragment() {
    private lateinit var viewBinding: FragmentMainBinding
    private  val viewModel: MainViewModel = MainViewModel()
    private lateinit var  adapter: NotesRecyclerViewAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewBinding = FragmentMainBinding.bind(
            LayoutInflater.from(context).inflate(R.layout.fragment_main, container, false)
        )
        return viewBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = NotesRecyclerViewAdapter(
            onClick = {
                    note -> findNavController().navigateSafe(MainFragmentDirections.toNoteDetails(note))
            },
            onDelete = {
                viewModel.deleteNote(it)
            }
        )
        viewBinding.recyclerView.adapter = adapter
        viewModel.listLiveData.observe(this.viewLifecycleOwner){
            adapter.submitList(it)
        }

        setFragmentResultListener(NoteDetailsFragment.NOTE_RESULT) { key, bundle ->
            bundle.getParcelable<Note>(NoteDetailsFragment.NOTE)?.let {
                if(it.id<0){
                    viewModel.addNote(it)
                    viewBinding.recyclerView.scrollToPosition(1)
                } else{
                    viewModel.editNote(it)
                }
            }
        }

        viewBinding.btnAdd.setOnClickListener {
            findNavController().navigateSafe(MainFragmentDirections.toNoteDetails(null))
        }

    }

}