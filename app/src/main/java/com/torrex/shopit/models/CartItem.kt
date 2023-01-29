package com.torrex.shopit.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CartItem(
    val userId:String="",
    val productOwnerId:String="",
    val productId:String="",
    val title:String="",
    val price:Long=0,
    val image:String="",
    var cartQuantity:Int=0,
    var stockQuantity:Int=0,
    var id:String="",
):Parcelable