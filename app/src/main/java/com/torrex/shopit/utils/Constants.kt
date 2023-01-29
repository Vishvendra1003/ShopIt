package com.torrex.shopit.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore.Images.Media
import android.webkit.MimeTypeMap
import com.torrex.shopit.models.User

object Constants {

    //---------------------------------------------------------------------
    var currentLoggedInUser:String=""

    const val shippingCharges:Int=0

    //----------------------------------------------UPI PAYMENT DETAILS--------------------------------
    const val payment_upi_address:String="8840344272@paytm"
    const val payment_merchant_code:String="8840344272"
    const val payment_description="Payment to the account for the Product"

    //Database
    var profileUser: User? =null
    const val USERS:String="users"
    const val PRODUCTS:String="products"
    const val USER_ADDRESS:String="UserAddress"
    const val ORDERS:String="orders"
    const val PAYMENT_DETAILS:String="payment_details"
    const val CARTITEMS:String="cartItems"
    const val ACCOUNTDETAILS:String="accountDetails"
    const val SELLERACCOUNT:String="sellerAccount"

    const val MALE:String="male"
    const val FEMALE:String="female"

    //variable for the FireBase Storage for User
    const val ID:String="id"
    const val EMAIL:String="email"
    const val FIRSTNAME:String="firstName"
    const val LASTNAME:String="lastName"
    const val MOBILE:String="mobile"
    const val GENDER:String="gender"
    const val DOB:String="dob"
    const val IMAGE:String="image"
    const val PROFILECOMPLETE:String="profileCompleted"


    //variable for the FireBase Storage for Products
    const val USERID:String="userId"
    const val PRODUCTNAME:String="productName"
    const val PRODUCTPRICE:String="productPrice"
    const val PRODUCTQUANTITY:String="productQuantity"
    const val PRODUCTDESCRIPTION:String="productDescription"
    const val PRODUCTIMAGE:String="productImage"
    const val PRODUCTLIST:String="productList"
    const val PRODUCTID:String="productId"


    //Variables for the Firebase Storage for the Address
    const val ADDRESS_USER_ID:String="userid"
    const val ADDRESS_LINE_1:String="addressLine1"
    const val ADDRESS_LINE2:String="addressLine3"
    const val ADDRESS_LINE3:String="addressCity"
    const val ADDRESS_STATE:String="addressState"
    const val ADDRESS_POSTAL_CODE:String="addressPostalCode"
    const val ADDRESS_MOBILE_NO:String="addressMobileNO"
    const val ADDRESS_ID:String="addressId"



    //Button Constant name
    const val BTN_EDIT:String="Edit"
    const val BTN_SAVE:String="Save"
    const val BTN_SUBMIT:String="Submit"

    const val USER_PROFILE_IMAGE:String="user_profile_image"
    const val PRODUCT_IMAGE:String="product_image"


    //For transferring data from one activity to another--------------------------------------------------
    const val PROFILE_USER_DETAILS:String="profile_user_details"
    const val EXTRA_PRODUCT_DETAILS:String="extra_product_details"
    const val EXTRA_PRODUCT_ID:String="extra_product_id"
    const val EXTRA_PRODUCT_USER_ID:String="extra_product_user_id"
    const val EXTRA_ORDER_DETAILS_PAYMENT:String="extra_order_details_payment"
    const val EXTRA_CART_ITEM_ID:String="extra_cart_item_id"


    //Variables for the Order Summary--
    const val ORDER_SUMMARY_ADDRESS:String="order_Summary_Address"
    const val ORDER_SUMMARY_PRODUCT:String="order_Summary_Product"
    const val ORDER_SUMMARY_USER:String="order_Summary_User"

    const val ORDER_PLACED_INVOICE:String="order_Placed_Invoice"




    //Permission Request Codes
    const val PICK_IMAGE_REQUEST_CODE=1
    const val READ_EXTERNAL_STORAGE_CODE=2
    const val PICK_IMAGE_REQUEST_CODE_ADD_MORE_IMAGE=3
    const val REQUEST_CAMERA_PERMISSION=4

    //Image Type Function
    const val IMAGE_EDIT:String="Edit"
    const val IMAGE_ADD:String="Add"



    //Global Function -----------------------------------------------------------

    fun showImageChooser(activity: Activity){

        val galleryIntent=Intent(Intent.ACTION_PICK,Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    fun getFileExtension(activity: Activity,uri:Uri?):String?{
        return  MimeTypeMap.getSingleton().getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }

    fun setCurrentProfileUser(user: User){
        profileUser =user
        currentLoggedInUser =user.id
    }
    fun getCurrentProfileUser(): User? {
        return profileUser
    }



}