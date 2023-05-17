package com.personal.homestayfinder.ui.home.roomdetails

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.personal.homestayfinder.databinding.DialogScheduleBinding
import java.util.Calendar


class ScheduleDialog(
    context: Context,
    private val roomDetailsViewModel: RoomDetailsViewModel,
    private val lifecycle : LifecycleOwner,
    private val currentUserId : String
) : Dialog(context) {
    private lateinit var binding: DialogScheduleBinding
    private lateinit var timeSchedule: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogScheduleBinding.inflate(LayoutInflater.from(context), null, false)
        setContentView(binding.root)
        binding.viewModel = roomDetailsViewModel
        binding.scheduleDialog = this@ScheduleDialog
        binding.lifecycleOwner  = lifecycle
        binding.tvCancel.setOnClickListener {
            roomDetailsViewModel.resetSchedule()
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        val width = (context.resources.displayMetrics.widthPixels * 0.95).toInt()
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        window?.setLayout(width, height)
    }

    fun pickDateTime() {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)
        val dateDialog = DatePickerDialog(
            context,{_, year, month, dayOfMonth ->
                timeSchedule = "$dayOfMonth/$month/$year"
                TimePickerDialog(context,
                    {_, hourOfDay, minute ->
                        timeSchedule = "$timeSchedule $hourOfDay:$minute"
                        roomDetailsViewModel.setTimeSchedule(timeSchedule)
                },currentHour, currentMinute,true).show()
            }
            ,currentYear, currentMonth, currentDay
        )
        dateDialog.show()
    }
    fun submitSchedule(){
        if(roomDetailsViewModel.isValidateSchedule()){
            roomDetailsViewModel.submitSchedule(currentUserId)
            roomDetailsViewModel.resetSchedule()
            dismiss()
        }
    }
}