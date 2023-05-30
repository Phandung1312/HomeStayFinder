package com.personal.homestayfinder.ui.favorite

import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.personal.homestayfinder.base.adapters.FavoriteRoomAdapter
import com.personal.homestayfinder.base.fragment.BaseFragment
import com.personal.homestayfinder.common.ItemRVClickListener
import com.personal.homestayfinder.data.models.RoomListItem
import com.personal.homestayfinder.databinding.FavoriteClass
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FavoriteFragment : BaseFragment<FavoriteClass>(FavoriteClass::inflate) {
    private val favoriteViewModel : FavoriteViewModel by viewModels()
    private lateinit var adapter: FavoriteRoomAdapter
    private lateinit var favoriteRoomsList : MutableList<RoomListItem>
    private lateinit var layoutClick: ItemRVClickListener
    private lateinit var ivbClick : ItemRVClickListener
    override fun initView() {
        showBottomNavView()
        layoutClick = object : ItemRVClickListener{
            override fun onClick(view: View, position : Int,item: Any) {
                val roomId = item as String
                findNavController().navigate(
                    FavoriteFragmentDirections
                    .actionFavoriteFragmentToRoomDetailsFragment(roomId))
            }
        }
        ivbClick = object : ItemRVClickListener{
            override fun onClick(view: View,position : Int, item: Any) {
                val roomId = item as String
                favoriteViewModel.unFavorite(currentUser.uid, roomId).observe(viewLifecycleOwner){
                    favoriteRoomsList.removeAt(position)
                    adapter.notifyItemRemoved(position)
                   Handler(Looper.getMainLooper()).post {
                        binding.layoutEmpty.visibility = if(favoriteRoomsList.isEmpty()) View.VISIBLE else View.GONE
                    }
                }
            }

        }
        favoriteRoomsList  = arrayListOf()
        adapter = FavoriteRoomAdapter(favoriteRoomsList, layoutClick, ivbClick)
        binding.rvFavoriteRooms.adapter = adapter
    }

    override fun initListeners() {

    }

    override fun initData() {
        registerObserverSmallLoadingEvent(favoriteViewModel, viewLifecycleOwner)
        registerAllExceptionEvent(favoriteViewModel,viewLifecycleOwner)
        favoriteViewModel.getAllFavorite(currentUser.uid)
        favoriteViewModel.favoriteRooms.observe(viewLifecycleOwner){ favoriteRooms ->
            favoriteRoomsList.clear()
            if(favoriteRooms.isNotEmpty()){
                binding.layoutEmpty.visibility = View.GONE
                favoriteRoomsList.addAll(favoriteRooms)
            }
            else{
                binding.layoutEmpty.visibility = View.VISIBLE
            }
            adapter.notifyDataSetChanged()
        }
    }
}