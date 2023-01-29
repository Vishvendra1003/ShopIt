package com.torrex.shopit.utils

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.widget.TextView
import java.util.*

class CustomDatePicker(val context: Context) {

    @SuppressLint("SetTextI18n")
    fun datePickerCustom(datePicked: TextView){
        val calender= Calendar.getInstance()
        val year=calender.get(Calendar.YEAR)
        val month=calender.get(Calendar.MONTH)
        val day=calender.get(Calendar.DAY_OF_MONTH)


        val datePickerDialog= DatePickerDialog(context,{ view, year, month, day->
            datePicked.text= "$day/$month/$year"
        },year,month,day)
        datePickerDialog.show()
    }
}