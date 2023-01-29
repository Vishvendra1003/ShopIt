package com.torrex.shopit.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val id:String="",
    val firstName:String="",
    val lastName:String="",
    val email:String="",
    val mobile:Long=0,
    val gender:String="",
    val dob:String="",
    val image:String="",
    val profileCompleted:Int=0,
    var sellerAccount:Boolean=false
):Parcelable