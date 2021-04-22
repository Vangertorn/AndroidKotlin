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
        viewModel.notesLiveData.observe(this.viewLifecycleOwner) {
            adapter.submitList(it)
        }
        viewBinding.recyclerView.postDelayed({
            viewBinding.recyclerView.scrollToPosition(0)
        }, 600)

        viewBinding.ivLogout.setOnClickListener {
            viewModel.logout()
            findNavController().navigateSafe(MainFragmentDirections.actionMainFragmentToEnterFragment())

        }


        viewBinding.btnAdd.setOnClickListener {
            findNavController().navigateSafe(MainFragmentDirections.toNoteDetails())
        }


    }

}