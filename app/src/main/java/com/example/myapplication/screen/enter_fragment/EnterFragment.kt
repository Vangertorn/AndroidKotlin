package com.example.myapplication.screen.enter_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentEnterBinding
import com.example.myapplication.support.navigateSafe
import org.koin.android.viewmodel.ext.android.viewModel

class EnterFragment : Fragment() {
    private lateinit var viewBinding: FragmentEnterBinding
    private val viewModel: EnterViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentEnterBinding.bind(
            LayoutInflater.from(context).inflate(R.layout.fragment_enter, container, false)
        )
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.errorLiveData.observe(this.viewLifecycleOwner) { errorText ->
            Toast.makeText(requireContext(), errorText, Toast.LENGTH_SHORT).show()
        }
        viewModel.logGedIn.observe(this.viewLifecycleOwner) { loggedIn ->
            if (loggedIn) {
                findNavController().navigateSafe(EnterFragmentDirections.actionEnterFragmentToMainFragment())
            } else {
                viewBinding.root.alpha = 1f
            }
        }
        viewBinding.btnEnter.setOnClickListener {
            viewModel.login(viewBinding.editUserName.text.toString())
        }
        viewModel.autoCompleteUserNamesLiveData.observe(this.viewLifecycleOwner) { userNames ->
            val arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                requireContext(),
                android.R.layout.select_dialog_multichoice,
                userNames
            )
            viewBinding.editUserName.setAdapter(arrayAdapter)
        }
    }
}