package com.example.myapplication.screen.main


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentMainBinding
import com.example.myapplication.support.navigateSafe
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainFragment : Fragment() {
    private lateinit var viewBinding: FragmentMainBinding

    @ExperimentalCoroutinesApi
    private val viewModel: MainViewModel by viewModel()

    private val handler = Handler(Looper.getMainLooper())

    @ExperimentalCoroutinesApi
    private val adapter = NotesRecyclerViewAdapter(
        onClick = { note ->
            findNavController().navigateSafe(MainFragmentDirections.toNoteDetails(note))
        })
    @ExperimentalCoroutinesApi
    private val simpleCallback = MainSwipeCallback { position, direction ->
        when (direction) {
            ItemTouchHelper.LEFT -> {
                deleteNote(position)
            }
            ItemTouchHelper.RIGHT -> {
                deleteNote(position)
            }
        }
    }

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

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.recyclerView.adapter = adapter
        viewModel.notesLiveData.observe(this.viewLifecycleOwner) {
            adapter.submitList(it)
        }
        viewBinding.recyclerView.postDelayed({
            viewBinding.recyclerView.scrollToPosition(0)
        }, 600)


        viewBinding.btnAdd.setOnClickListener {
            findNavController().navigateSafe(MainFragmentDirections.toNoteDetails())
        }
        viewBinding.ivCloud.setOnClickListener {
            showCloudDialog()
        }
        viewBinding.ivSettings.setOnClickListener {
            findNavController().navigateSafe(MainFragmentDirections.actionMainFragmentToSettingFragment())
        }

        viewModel.userNameLiveDate.observe(this.viewLifecycleOwner) {
            val name = viewBinding.userName
            name.text = it
        }

        viewModel.progressLiveDate.observe(this.viewLifecycleOwner) {
            if (it.not()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.Failed_to_perform_cloud_operation),
                    Toast.LENGTH_LONG
                ).show()
            }
            viewBinding.indicatorProgress.isVisible = false
        }

        val noteHelper = ItemTouchHelper(simpleCallback)
        noteHelper.attachToRecyclerView(viewBinding.recyclerView)
    }

    @ExperimentalCoroutinesApi
    private fun deleteNote(position: Int) {
        var permissionDelete = true
        val deleteNote = viewModel.getNoteFromPosition(position)
        viewModel.deleteNoteFromPosition(position)
        adapter.notifyItemRemoved(position)

        Snackbar.make(
            viewBinding.recyclerView,
            getString(R.string.note_was_delete),
            Snackbar.LENGTH_LONG
        ).setAction(getString(R.string.undo)) {
            permissionDelete = viewModel.recoverNoteFromPosition(position, deleteNote!!)
            adapter.notifyItemInserted(position)

        }.show()
        handler.postDelayed(Runnable {
            if (permissionDelete) viewModel.deleteNote(
                deleteNote!!
            )
        }, 2750)
    }


    @ExperimentalCoroutinesApi
    private fun showCloudDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.Cloud_storage))
            .setMessage(getString(R.string.pick_cloud_action))
            .setPositiveButton(getString(R.string.Import)) { dialog, _ ->
                viewBinding.indicatorProgress.isVisible = true
                viewModel.importNotes()
                dialog.cancel()
            }.setNegativeButton(getString(R.string.Export)) { dialog, _ ->
                viewBinding.indicatorProgress.isVisible = true
                viewModel.exportNotes()
                dialog.cancel()
            }.show()
    }

}