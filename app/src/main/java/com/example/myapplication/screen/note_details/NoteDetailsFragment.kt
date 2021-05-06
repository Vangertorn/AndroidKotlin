package com.example.myapplication.screen.note_details

import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentNoteDetailsBinding
import com.example.myapplication.models.Note
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog
import org.koin.android.viewmodel.ext.android.viewModel

import java.text.SimpleDateFormat
import java.util.*

class NoteDetailsFragment : Fragment() {
    private lateinit var viewBinding: FragmentNoteDetailsBinding

    private val dateFormatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    private val args: NoteDetailsFragmentArgs by navArgs()
    private val viewModel: NoteDetailsViewModel by viewModel()
    private var noteDate = Date()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentNoteDetailsBinding.bind(
            LayoutInflater.from(context).inflate(R.layout.fragment_note_details, container, false)
        )
        return viewBinding.root

    }

    @RequiresApi(Build.VERSION_CODES.N)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        viewBinding.confirm.setOnClickListener {
            if (viewBinding.textNote.text.isNotBlank()) {
                args.note?.let {
                    viewModel.updateNote(
                        Note(
                            id = it.id,
                            title = viewBinding.textNote.text.toString(),
                            date = dateFormatter.format(noteDate),
                            userName = it.userName
                        )
                    )
                } ?: kotlin.run {
                    viewModel.addNewNote(
                        Note(
                            title = viewBinding.textNote.text.toString(),
                            date = dateFormatter.format(noteDate),
                            userName = ""
                        )
                    )
                }
                findNavController().popBackStack(R.id.mainFragment, false)

            } else {
                Toast.makeText(requireContext(), "Could You enter note, please", Toast.LENGTH_LONG)
                    .show()
            }

        }

        args.note?.let { note ->
            viewBinding.textNote.setText(note.title)
            noteDate = dateFormatter.parse(note.date) ?: Date()
            viewBinding.tvTime.selectDate(java.util.Calendar.getInstance().apply { this.time = noteDate })
        }

        viewBinding.tvTime.addOnDateChangedListener { displayed, date ->
            noteDate = date
        }
    }
//    @RequiresApi(Build.VERSION_CODES.N)
//    private fun DatePicker.getSelectedDate(): Date {
//        val calendar = Calendar.getInstance(Locale.getDefault())
//        calendar.set(Calendar.YEAR, this.year)
//        calendar.set(Calendar.MONTH, this.month)
//        calendar.set(Calendar.DAY_OF_MONTH, this.dayOfMonth)
//        calendar.set(Calendar.HOUR, 0)
//        calendar.set(Calendar.MINUTE, 0)
//        calendar.set(Calendar.SECOND, 0)
//        calendar.set(Calendar.MILLISECOND, 0)
//        return calendar.time
//    }
//
//    @RequiresApi(Build.VERSION_CODES.N)
//    private fun DatePicker.setSelectionDate(date: String?) {
//        date?.let {
//            dateFormatter.parse(it)?.let { date ->
//                val calendar = Calendar.getInstance(Locale.getDefault())
//                calendar.time = date
//                val year = calendar.get(Calendar.YEAR)
//                val month = calendar.get(Calendar.MONTH)
//                val day = calendar.get(Calendar.DAY_OF_MONTH)
//                val hour = calendar.get(Calendar.HOUR_OF_DAY)
//                val minute = calendar.get(Calendar.MINUTE)
//                this.updateDate(year, month, day)
//            }
//        }
//    }

    companion object {
        const val NOTE = "NOTE"
    }

}