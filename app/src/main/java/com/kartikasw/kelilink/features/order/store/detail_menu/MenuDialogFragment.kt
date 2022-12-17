package com.kartikasw.kelilink.features.order.store.detail_menu

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.kartikasw.kelilink.R
import com.kartikasw.kelilink.core.data.helper.Constants.Extra.EXTRA_MENU_ID
import com.kartikasw.kelilink.core.domain.Resource
import com.kartikasw.kelilink.core.domain.model.Menu
import com.kartikasw.kelilink.databinding.DialogMenuBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuDialogFragment: DialogFragment() {

    private var _binding: DialogMenuBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MenuDialogViewModel by viewModels()

    private lateinit var menuId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.Kelilink_Dialog_Alert_NoFloating)

        menuId = requireArguments().getString(EXTRA_MENU_ID)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getMenuById(menuId).observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    setUpMenuView(it.data!!)
                }
                is Resource.Error -> {
                    Log.e(TAG, it.message.toString())
                }
                else -> {}
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUpMenuView(menu: Menu) {
        with(binding) {
            Glide.with(dmIvMenu.context)
                .load(menu.image)
                .placeholder(R.drawable.shimmer_placeholder)
                .transform(CenterCrop(), RoundedCorners(resources.getDimensionPixelSize(R.dimen.corner_image)))
                .into(dmIvMenu)

            dmTvTitle.text = menu.name
            dmTvPrice.text = "${menu.price}/${menu.unit}"
            dmTvDescription.text = menu.description
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val TAG = "MenuDialogFragment"
    }

}