package com.torrex.shopit.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AccountDetails(
    val userId:String="",
    val payeeVpa:String="",
    val payeeName:String="",
    val payeeMerchantCode:String="",
):Parcelable