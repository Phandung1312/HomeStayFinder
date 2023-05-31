package com.personal.homestayfinder.ui.chat.message

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.provider.Settings
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.personal.homestayfinder.R
import com.personal.homestayfinder.base.adapters.ImageAdapter
import com.personal.homestayfinder.base.adapters.MessageAdapter
import com.personal.homestayfinder.base.dialogs.ImageMessageDialog
import com.personal.homestayfinder.base.fragment.BaseFragment
import com.personal.homestayfinder.common.ItemRoomClickListener
import com.personal.homestayfinder.data.models.Message
import com.personal.homestayfinder.data.models.RoomListItem
import com.personal.homestayfinder.data.models.toUser
import com.personal.homestayfinder.databinding.MessageClass
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MessageFragment : BaseFragment<MessageClass>(MessageClass::inflate) {
    private val messageViewModel : MessageViewModel by viewModels()
    private val args : MessageFragmentArgs by navArgs()
    private lateinit var itemChangeListener : ItemChangeListener
    private lateinit var messageImageClickListener: MessageImageClickListener
    private lateinit var itemRoomClickListener : ItemRoomClickListener
    private lateinit var urisList : MutableList<Uri>
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var imageAdapter : ImageAdapter
    override fun initView() {
        hideBottomNavView()
        urisList = ArrayList()
        imageAdapter = ImageAdapter(messageViewModel, urisList)
        binding.apply {
            messageFragment = this@MessageFragment
            viewModel  = messageViewModel
            rvMessageImages.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rvMessageImages.adapter = imageAdapter
        }
    }
    fun openGalley(){
        Dexter.withContext(activity).withPermission(
            android.Manifest.permission.READ_MEDIA_IMAGES
        ).withListener(object : PermissionListener {
            override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                    type = "image/*"
                    putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                }
                pickImageFromGalleryForResult.launch(intent)
            }
            override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                Toast.makeText(activity,"You have denied the storage permission select image",
                    Toast.LENGTH_SHORT).show()
                showRotationalDialogForPermission()
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: PermissionRequest?,
                p1: PermissionToken?
            ) {
                showRotationalDialogForPermission()
            }

        }).onSameThread().check()
    }
    private fun showRotationalDialogForPermission(){
        AlertDialog.Builder(activity)
            .setMessage("Bật quyền trong cài đặt ứng dụng")
            .setPositiveButton("Vào cài đặt"){_,_->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package",activity?.packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }
                catch (e : ActivityNotFoundException){
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Hủy"){dialog,_->
                dialog.dismiss()
            }.show()
    }
    override fun initListeners() {
        imageAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver(){
            override fun onChanged() {
                binding.rvMessageImages.visibility = if(imageAdapter.itemCount > 0) View.VISIBLE else View.GONE
            }
        })
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        itemChangeListener = object : ItemChangeListener{

            override fun seen(message: Message) {
                messageViewModel.seenMessage(message.sender!!, message.receiver!!)
            }

            override fun onScrollRecyclerView(position: Int) {
                binding.rvMessages.scrollToPosition(position - 1)
            }

        }
        messageImageClickListener = object : MessageImageClickListener{
            override fun onClick(imgUrl: String) {
                val dialog = ImageMessageDialog(requireContext(),imgUrl)
                dialog.show()
            }
        }
        itemRoomClickListener = object : ItemRoomClickListener {
            override fun onRoomClick(room: RoomListItem) {
                findNavController().navigate(MessageFragmentDirections
                    .actionMessageFragmentToRoomDetailsFragment(room.id!!))
            }
        }
        binding.edInputMessage.setOnEditorActionListener{ _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                sendMessage()
                true
            } else {
                false
            }
        }
        registerObserverSmallLoadingEvent(messageViewModel, viewLifecycleOwner)
    }

    override fun initData() {
        messageViewModel.getReceiverInfo(args.id)
        messageViewModel.receiver.observe(viewLifecycleOwner){
          setMessageAdapter()
        }
    }
    private fun setMessageAdapter(){
        val options = FirebaseRecyclerOptions.Builder<Message>()
            .setQuery(messageViewModel.getReferentCurrentUserChat(currentUser.uid,
                messageViewModel.receiver.value!!.userId!!), Message::class.java)
            .setLifecycleOwner(this)
            .build()
        messageAdapter = MessageAdapter(options,
        currentUser.toUser(),
        messageViewModel.receiver.value!!,
        itemChangeListener,
        messageImageClickListener,
        itemRoomClickListener)
        binding.rvMessages.adapter = messageAdapter
        messageAdapter.startListening()
        scrollRvWhenUserType()
    }
    private fun scrollRvWhenUserType(){
            // Do not databinding.edInputMessage here
            val editText = activity?.findViewById<EditText>(R.id.ed_input_message)
            val viewTreeObserver = editText?.viewTreeObserver
            val r = Rect()
            viewTreeObserver?.addOnGlobalLayoutListener {
                editText.getWindowVisibleDisplayFrame(r)
                val heightDiff = editText.rootView?.height?.minus((r.bottom - r.top))
                if (heightDiff != null) {
                    if (heightDiff > 100) {
                        binding.rvMessages.scrollToPosition(messageAdapter.itemCount - 1)
                    }
                }
            }
    }
    fun sendMessage(){
        if(binding.edInputMessage.text.toString().isNotBlank() or urisList.isNotEmpty()){
            messageViewModel.sendMessage(currentUser.uid)
            binding.edInputMessage.setText("")
            urisList.clear()
            imageAdapter.notifyDataSetChanged()
        }
    }
    private val pickImageFromGalleryForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
        if (result.resultCode == Activity.RESULT_OK){
            result.data?.clipData?.let {
                val count = it.itemCount
                for(i in 0 until count){
                    if(urisList.size > 10){
                        Toast.makeText(requireContext(),"Chỉ cho phép gửi tối đa 10 ảnh", Toast.LENGTH_SHORT).show()
                        break
                    }
                    urisList.add(it.getItemAt(i).uri)
                }
                imageAdapter.updateList()
            } ?: result.data?.data?.let{
                if(urisList.size > 10){
                    Toast.makeText(requireContext(),"Chỉ cho phép gửi tối đa 10 ảnh", Toast.LENGTH_SHORT).show()
                }else{
                    urisList.add(it)
                    imageAdapter.updateList()
                }
            }
        }
    }
}