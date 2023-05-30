package com.personal.homestayfinder.base.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.personal.homestayfinder.base.adapters.AddressAdapter
import com.personal.homestayfinder.data.models.Location
import com.personal.homestayfinder.databinding.DialogAddressBinding

class AddressDialog(
    context : Context,
    private val locationsList : List<Location>,
    var currentLocation : Location,
    private val fragmentCallback : AddressCallBack,
    private val title : String,
    private val positiveButtonTitle: String,
    private val negativeButtonTitle: String? = null
) : Dialog(context) {
    private lateinit var binding : DialogAddressBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogAddressBinding.inflate(LayoutInflater.from(context), null, false)
        setContentView(binding.root)
        initView()
        setAdapter()
        initListeners()

    }

    override fun onStart() {
        super.onStart()
        val width = (context.resources.displayMetrics.widthPixels * 0.90).toInt()
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        window?.setLayout(width, height)
    }
    private fun initView(){
        binding.apply {
            tvTitle.text = title
            btnConfirm.text = positiveButtonTitle
            negativeButtonTitle?.let {
                btnCancel.text = it
                btnCancel.visibility = View.VISIBLE
            }
        }
    }
    private fun initListeners(){
        binding.apply {
            btnConfirm.setOnClickListener {
                fragmentCallback.onLocationSelected(currentLocation)
                dismiss()
            }
            btnCancel.setOnClickListener {
                dismiss()
            }
        }
    }
    private fun setAdapter(){
        val dialogCallBack = object : AddressCallBack{
            override fun onLocationSelected(location: Location) {
                currentLocation = location
            }

        }
        val adapter = AddressAdapter(locationsList,currentLocation,dialogCallBack)
        binding.rvAddresses.adapter = adapter
    }
    interface AddressCallBack {
        fun onLocationSelected(location : Location)
    }
}