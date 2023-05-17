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
        dataBinding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        dataBinding.viewModel = viewModel
        dataBinding.roomAddressFragment = this@RoomAddressFragment
    }

    override fun initListeners() {
    }

    override fun initData() {
        viewModel.getAllCity()
    }
    fun goToNextScreen(){
        if(viewModel.isValidateAddress()){
            findNavController().navigate(R.id.action_roomAddressFragment_to_roomUtilitiesFragment)
        }
    }
}