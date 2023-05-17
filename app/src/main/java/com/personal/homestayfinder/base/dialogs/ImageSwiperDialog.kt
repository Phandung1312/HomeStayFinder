package com.personal.homestayfinder.base.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.personal.homestayfinder.R
import com.personal.homestayfinder.adapters.ImageSwiperAdapter
import com.personal.homestayfinder.databinding.DialogImageSwiperBinding

class ImageSwiperDialog constructor(
    private val urlsList : List<String>,
    private val position : Int
): DialogFragment() {
    private lateinit var binding : DialogImageSwiperBinding
    private lateinit var adapter : ImageSwiperAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogImageSwiperBinding.inflate(layoutInflater)
        initView()
        binding.btnClose.setOnClickListener {
            dismiss()
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawableResource(R.color.transparent)
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }
    fun initView(){
        adapter = ImageSwiperAdapter(urlsList)
        binding.viewPager.adapter = adapter
        binding.viewPager.currentItem = position
    }
}