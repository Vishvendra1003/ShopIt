package com.torrex.shopit.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order (
    val userId:String="",
    val product: Product = Product(),
    val address: Address = Address(),
    val title:String="",
    val image:String="",
    val productQuantity:Int=0,
    val subTotalAmount:Long=0,
    val shippingCharge:Int=0,
    val totalAmount:Long=0,
    val orderDatetime:String="",
    var orderPaymentTransactionId:String="",
    var orderPlaced:Boolean=false,
    val id:String=""
):Parcelable