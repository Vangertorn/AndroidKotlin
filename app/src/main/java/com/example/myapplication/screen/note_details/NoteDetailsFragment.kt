package com.example.myapplication.screen.note_details

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentNoteDetailsBinding
import com.example.myapplication.models.Note
import com.example.myapplication.support.SupportFragmentInset
import com.example.myapplication.support.hideKeyboard
import com.example.myapplication.support.setVerticalMargin
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*


class NoteDetailsFragment :
    SupportFragmentInset<FragmentNoteDetailsBinding>(R.layout.fragment_note_details) {
    override val viewBinding: FragmentNoteDetailsBinding by viewBinding()

    private val dateFormatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    private val args: NoteDetailsFragmentArgs by navArgs()
    private val viewModel: NoteDetailsViewModel by viewModel()
    private var noteDate = Date()


    @RequiresApi(Build.VERSION_CODES.N)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewBinding.toolbar.setNavigationOnClickListener {
            hideKeyboard()
            findNavController().popBackStack()
        }
        viewBinding.confirm.setOnClickListener {
            if (viewBinding.textNote.text.isNotBlank()) {
                args.note?.let {
                    viewModel.updateNote(
                        Note(
                            id = it.id,
                            title = viewBinding.textNote.text.toString(),
                            date = dateFormatter.format(noteDate),
                            userName = it.userName,
                            alarmEnabled = viewBinding.alarmSwitch.isChecked,
                            postscript = it.postscript,
                            position = it.position
                        )
                    )
                } ?: kotlin.run {
                    viewModel.addNewNote(
                        Note(
                            title = viewBinding.textNote.text.toString(),
                            date = dateFormatter.format(noteDate),
                            userName = "",
                            alarmEnabled = viewBinding.alarmSwitch.isChecked
                        )
                    )
                }
                hideKeyboard()
                findNavController().popBackStack(R.id.mainFragment, false)

            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.could_you_enter_note_please),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }

        args.note?.let { note ->
            viewBinding.alarmSwitch.isChecked = note.alarmEnabled
            viewBinding.textNote.setText(note.title)
            noteDate = dateFormatter.parse(note.date) ?: Date()
            viewBinding.tvTime.selectDate(
                Calendar.getInstance().apply { this.time = noteDate })
        }

        viewBinding.tvTime.addOnDateChangedListener { displayed, date ->
            noteDate = date
        }
    }

    override fun onInsetsReceived(top: Int, bottom: Int, hasKeyboard: Boolean) {
        viewBinding.toolbar.setVerticalMargin(top)
        viewBinding.confirm.setVerticalMargin(0, bottom)
    }

}