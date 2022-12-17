package com.kartikasw.kelilink.features.order.store.note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.kartikasw.kelilink.R
import com.kartikasw.kelilink.core.data.helper.Constants.Extra.EXTRA_MENU_ID
import com.kartikasw.kelilink.core.data.helper.Constants.Extra.EXTRA_MENU_NOTE
import com.kartikasw.kelilink.databinding.FragmentNoteBinding
import com.kartikasw.kelilink.features.order.store.StoreViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NoteFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!

    private lateinit var menuId: String
    private lateinit var menuNote: String

    private val viewModel: StoreViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        menuId = arguments?.getString(EXTRA_MENU_ID)!!
        menuNote = arguments?.getString(EXTRA_MENU_NOTE)!!
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nEtNote.setText(menuNote)

        binding.nBtnSave.setOnClickListener {
            viewModel.updateNoteMenu(menuId, binding.nEtNote.text.toString())
            dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val TAG = "AddNoteFragment"
    }

}