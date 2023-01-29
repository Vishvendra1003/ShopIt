package com.torrex.shopit.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
    val userId:String="",
    val productName:String="",
    val productPrice:Long=0,
    val productQuantity:Int=0,
    val productDescription:String="",
    val productImage:String="",
    val productList:ArrayList<String> =ArrayList<String>(),
    val productId:String=""
):Parcelable