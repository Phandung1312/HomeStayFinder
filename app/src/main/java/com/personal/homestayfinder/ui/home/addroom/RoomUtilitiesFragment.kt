package com.personal.homestayfinder.ui.home.addroom

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import com.personal.homestayfinder.R
import com.personal.homestayfinder.adapters.ImageAdapter
import com.personal.homestayfinder.base.fragment.BaseFragment
import com.personal.homestayfinder.databinding.RoomUtilitiesClass
import java.io.IOException

class RoomUtilitiesFragment : BaseFragment<RoomUtilitiesClass>(RoomUtilitiesClass::inflate) {
    private val addRoomViewModel by activityViewModels<AddRoomViewModel>()
    private lateinit var adapter : ImageAdapter
    private lateinit var urisList : MutableList<Uri>
    override fun initView() {
        dataBinding.apply {
            roomUtilitiesFragment = this@RoomUtilitiesFragment
            viewModel = addRoomViewModel
        }
        urisList = addRoomViewModel.imagesList.value?.toList()?.toMutableList() ?: ArrayList()
        adapter = ImageAdapter(addRoomViewModel, urisList)
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver(){
            override fun onChanged() {
                addRoomViewModel.isVisible.value = adapter.itemCount > 0
            }
        })
        val layoutManager = GridLayoutManager(requireContext(), 3)
        dataBinding.rvRoomImages.layoutManager = layoutManager
        dataBinding.rvRoomImages.adapter = adapter
        adapter.updateList()
    }

    override fun initListeners() {
        dataBinding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun initData() {

    }
    fun openGalley(){
        Dexter.withContext(activity).withPermission(
            android.Manifest.permission.READ_EXTERNAL_STORAGE
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
    fun openCamera(){
        Dexter.withContext(activity)
            .withPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA).withListener(
                object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                        p0?.let {
                            if(p0.areAllPermissionsGranted()){
                                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                takePhotoForResult.launch(intent)
                            }
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: MutableList<PermissionRequest>?,
                        p1: PermissionToken?
                    ) {
                        showRotationalDialogForPermission()
                    }

                }
            ).onSameThread().check()
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
    fun deleteAllImages(){
        urisList.clear()
        adapter.updateList()
    }
    private val pickImageFromGalleryForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.clipData?.let {
                val count = it.itemCount
                for(i in 0 until count){
                    if(urisList.size > 19){
                        Toast.makeText(requireContext(),"Chỉ cho phép thêm tối đa 20 ảnh", Toast.LENGTH_SHORT).show()
                        break
                    }
                    urisList.add(it.getItemAt(i).uri)
                }
                adapter.updateList()
            } ?: result.data?.data?.let{
                if(urisList.size > 19){
                    Toast.makeText(requireContext(),"Chỉ cho phép thêm tối đa 20 ảnh", Toast.LENGTH_SHORT).show()
                }else{
                    urisList.add(it)
                    adapter.updateList()
                }
            }
        }
    }
    private val takePhotoForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageBitmap = result.data?.extras?.get("data") as Bitmap
            val uri = try {
                val path = MediaStore.Images.Media.insertImage(requireContext().contentResolver, imageBitmap, "Title", null)
                Uri.parse(path)
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
            uri?.let{
                urisList.add(it)
                adapter.updateList()
            }
        }
    }
    fun goToNextScreen(){
        if(addRoomViewModel.isValidateUtilities()){
            findNavController().navigate(R.id.action_roomUtilitiesFragment_to_roomConfirmationFragment)
        }
    }
}