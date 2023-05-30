package com.personal.homestayfinder.ui.home.addroom

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.personal.homestayfinder.R
import com.personal.homestayfinder.base.fragment.BaseFragment
import com.personal.homestayfinder.databinding.RoomAddressClass
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoomAddressFragment : BaseFragment<RoomAddressClass>(RoomAddressClass::inflate) {
    private val viewModel : AddRoomViewModel by activityViewModels()
    override fun initView() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.viewModel = viewModel
        binding.roomAddressFragment = this@RoomAddressFragment
    }

    override fun initListeners() {
        registerAllExceptionEvent(viewModel,viewLifecycleOwner)
    }

    override fun initData() {
        viewModel.getAllCity(getCurrentArea().value?.id)
    }
    fun cancel(){
        findNavController().popBackStack()
        findNavController().popBackStack()
    }
    fun goToNextScreen(){
        if(viewModel.isValidateAddress()){
            findNavController().navigate(R.id.action_roomAddressFragment_to_roomUtilitiesFragment)
        }
    }
}