package com.torrex.shopit.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Address(
    val userid:String="",
    val addressLine1:String="",
    val addressLine2:String="",
    val addressLine3:String="",
    val addressCity:String="",
    val addressState:String="",
    val addressPostalCode:Int=0,
    val addressMobileNO:Long=0,
    val addressId:String=""
    ):Parcelable