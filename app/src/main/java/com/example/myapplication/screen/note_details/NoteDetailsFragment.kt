package com.example.myapplication.screen.note_details
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentNoteDetailsBinding
import com.example.myapplication.models.Note
import com.example.myapplication.support.hideKeyboard
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

}