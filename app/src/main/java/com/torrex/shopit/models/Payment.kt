package com.torrex.shopit.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Payment(
        val payeeVpa:String="",
        val payeeName:String="",
        val transactionId:String= "",
        val transactionRefId:String="",
        val amount:String="",
        val payeeMerchantCode:String="",
        val description:String="",
        var paymentStatus:String=""

):Parcelable