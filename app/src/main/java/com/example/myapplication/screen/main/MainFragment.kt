package com.example.myapplication.screen.main


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentMainBinding
import com.example.myapplication.support.navigateSafe
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainFragment : Fragment() {
    private lateinit var viewBinding: FragmentMainBinding
    private val viewModel: MainViewModel by viewModel()
    private val adapter = NotesRecyclerViewAdapter(
        onClick = { note ->
        findNavController().navigateSafe(MainFragmentDirections.toNoteDetails(note))
    },
        onDelete = {
            viewModel.deleteNote(it)
        })

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
        viewBinding.recyclerView.adapter = adapter
        viewModel.listLiveData.observe(this.viewLifecycleOwner) {
            adapter.submitList(it)
        }
        viewModel.invalidateList()


        viewBinding.btnAdd.setOnClickListener {
            findNavController().navigateSafe(MainFragmentDirections.toNoteDetails())
        }


    }

}