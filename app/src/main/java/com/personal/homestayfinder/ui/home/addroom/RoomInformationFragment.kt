package com.personal.homestayfinder.ui.home.addroom

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.personal.homestayfinder.R
import com.personal.homestayfinder.base.fragment.BaseFragment
import com.personal.homestayfinder.databinding.RoomInfoClass
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoomInformationFragment : BaseFragment<RoomInfoClass>(RoomInfoClass::inflate) {
    private val addRoomViewModel : AddRoomViewModel by activityViewModels()
    private val args : RoomInformationFragmentArgs by navArgs()
    override fun initView() {
        hideBottomNavView()
        dataBinding.apply {
            viewModel = addRoomViewModel
            roomInfoFragment = this@RoomInformationFragment
        }
    }

    override fun initListeners() {
        dataBinding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        registerObserverSmallLoadingEvent(addRoomViewModel, viewLifecycleOwner)
        registerAllExceptionEvent(addRoomViewModel,viewLifecycleOwner)
    }

    override fun initData() {
        if(args.roomId != null && !addRoomViewModel.isHasBeenInit.value!!){
            addRoomViewModel.setRoomData(args.roomId!!)
        }
    }
    fun goToNextScreen(){
        if(addRoomViewModel.isValidateRoomInfo()){
            findNavController().navigate(R.id.action_roomInformationFragment_to_roomAddressFragment)
        }
    }

    override fun onDetach() {
        super.onDetach()
        requireActivity().viewModelStore.clear()
    }
}