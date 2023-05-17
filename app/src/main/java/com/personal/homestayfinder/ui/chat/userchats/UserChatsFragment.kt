package com.personal.homestayfinder.ui.chat.userchats


import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.personal.homestayfinder.adapters.UserAdapter
import com.personal.homestayfinder.base.fragment.BaseFragment
import com.personal.homestayfinder.common.ItemRVClickListener
import com.personal.homestayfinder.data.models.User
import com.personal.homestayfinder.databinding.UserChatsClass
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserChatsFragment : BaseFragment<UserChatsClass>(UserChatsClass::inflate) {
    private val viewModel by viewModels<UserChatsViewModel>()
    private lateinit var itemRVClickListener: ItemRVClickListener
    override fun initView() {
    }

    override fun initListeners() {
        showBottomNavView()
        itemRVClickListener = object : ItemRVClickListener{
            override fun onClick(view: View,position : Int, item: Any) {
                val user = item as User
                findNavController().navigate(UserChatsFragmentDirections
                    .actionUserChatsFragmentToMessageFragment(user.userId!!))
            }

        }
        registerObserverSmallLoadingEvent(viewModel, viewLifecycleOwner)
        registerAllExceptionEvent(viewModel,viewLifecycleOwner)
    }

    override fun initData() {
        viewModel.getListIdUsers(currentUser.uid).observe(viewLifecycleOwner){ idUsers ->
            if(idUsers.isNotEmpty()){
                viewModel.getListUserChats(idUsers).observe(viewLifecycleOwner){ users ->
                    dataBinding.layoutEmpty.visibility = View.GONE
                    val adapter = UserAdapter(
                        users,
                        itemRVClickListener,
                        viewModel,
                        viewLifecycleOwner,
                        currentUser.uid
                    )
                    dataBinding.rvChats.adapter = adapter
                }
            }
            else{
                dataBinding.layoutEmpty.visibility = View.VISIBLE
            }
        }
    }
}