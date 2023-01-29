package com.torrex.shopit.firestore

import android.app.Activity
import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment
import com.torrex.shopit.MainActivity
import com.torrex.shopit.activities.ui.home.HomeFragment
import com.torrex.shopit.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.torrex.shopit.activities.*
import com.torrex.shopit.models.*
import kotlinx.coroutines.flow.merge


class FireStoreClass {

    private val mFireStore=FirebaseFirestore.getInstance()


    //Querying for the current user---------------------------------------------------
    fun getCurrentUserID():String{

        //instance for the current user
        val currentUser=FirebaseAuth.getInstance().currentUser

        var currentUserId=""
        if (currentUser!=null){
            currentUserId=currentUser!!.uid
        }

        return currentUserId
    }

    //Uploading the image
    fun uploadImageToCloudStorage(activity: Activity, imageFileURI: Uri,imageType:String){
        val sRef:StorageReference=FirebaseStorage.getInstance().reference
            .child(imageType+System.currentTimeMillis()+"."
                    + Constants.getFileExtension(activity,imageFileURI))

        sRef.putFile(imageFileURI!!).addOnSuccessListener { taskSnapShot->
            Log.e("Firebase Image URL", taskSnapShot.metadata!!.reference!!.downloadUrl.toString())

            taskSnapShot!!.metadata!!.reference!!.downloadUrl.addOnSuccessListener { url->
                Log.e("Downloadable LInk",url.toString())

                when(activity){
                    is RegisterNewUser ->{
                        activity.imageUploadSuccess(url.toString())
                    }
                    is ProfileActivity ->{
                        activity.imageUploadSuccess(url.toString())
                    }

                    is AddProductActivity ->{
                        activity.uploadProductImageSuccess(url.toString())
                    }
                    is ProductSettingActivity ->{
                        activity.uploadProductImageSuccess(url.toString())
                    }
                }
            }
        }.addOnFailureListener{exception->
            when(activity){
                is RegisterNewUser ->{
                    Log.e(activity.javaClass.simpleName,exception.message,exception)
                    Log.e("Image failure","Image Upload Fail..................")
                }
            }
        }
    }




//-----------------------------------------User Details-------------------------------------------------------------------------------

    //Registering new User to the Database------------------------
    fun registerUser(activity: RegisterNewUser, user: User){

        mFireStore.collection(Constants.USERS)
            .document(user.id)
            .set(user, SetOptions.merge())
            .addOnSuccessListener {

                activity.userRegisteredSuccessfully()
            }

            .addOnFailureListener { e->
                Log.e(activity.javaClass.simpleName,"error while registering the new user from Register User class",e)

            }
    }


    //Get Logged or current User--------------------------------------------
    fun getUserDetails(activity: Activity){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get().addOnSuccessListener { document->
                Log.i(activity.javaClass.simpleName,document.toString())

                //converting document into the User
                val user=document.toObject(User::class.java)!!

                //TODO("storing data in the mobile storage ")

                when(activity){
                    is LoginActivity ->{
                        activity.userLoggedInSuccess(user)
                    }

                    is MainActivity ->{
                        activity.userDetailsSuccess(user)
                    }
                    is DashboardActivity ->{
                        activity.getUserProfileSuccess(user)
                    }

                }

            }
            .addOnFailureListener {exception->
                when(activity){
                    is LoginActivity ->{
                        Log.e(activity.javaClass.simpleName,exception.message,exception)

                    }
                }
            }
    }


    //Update User Details------------------------------------------------------
    fun updateUserDetails(activity: Activity,userHashMap:HashMap<String,Any>){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .update(userHashMap)
            .addOnSuccessListener { document->
                when(activity){
                    is ProfileActivity ->{
                        activity.userProfileUpdateSuccess()
                    }
                    is PaymentDetailActivity->{
                        activity.userProfileUpdateSuccess()
                    }

                }
            }
    }



//-------------------------------------------Product Details--------------------------------------------------------------------------

    //Register new Product-----
    fun registerProduct(activity: AddProductActivity, product: Product){

        var mProductId:String=""
        mFireStore.collection(Constants.PRODUCTS)
                //use add to update product Id or document Id ----
            .add(product)
            .addOnSuccessListener{document->
                mProductId=document.id

                //updating document and product Id
                mFireStore.collection(Constants.PRODUCTS)
                    .document(mProductId)
                    .update("productId",mProductId)
                    .addOnSuccessListener {
                     activity.uploadProductSuccess()
                    }
                    .addOnFailureListener {e->
                        Log.d("Error Product Id", e.toString())
                    }

            }
            .addOnFailureListener { e->
                Log.e(activity.javaClass.simpleName,"Error while registering details for product",e)
            }

    }

    //Get product from activity--------------
    fun getProductList(activity: Activity){
        val user: User? = Constants.getCurrentProfileUser()
        mFireStore.collection(Constants.PRODUCTS)
            .whereEqualTo(Constants.USERID,user!!.id)
            .get()
            .addOnSuccessListener { document->
                val productList:java.util.ArrayList<Product> =ArrayList()
                for (i in document.documents){
                    val product=i.toObject(Product::class.java)
                    productList.add(product!!)
                }

                when(activity){
                    is MySellProduct ->{
                        activity.getProductListSuccess(productList)
                    }
                }
            }
    }

    //Get Product list form the fragment---------------
    fun getHomeProductList(fragment:Fragment){
        mFireStore.collection(Constants.PRODUCTS)
            .get()
            .addOnSuccessListener { document->
                val productList:ArrayList<Product> =ArrayList()

                for (i in document.documents){
                    val product=i.toObject(Product::class.java)
                    productList.add(product!!)
                }

                when(fragment){
                    is HomeFragment ->{
                        fragment.getHomeItemListSuccess(productList)
                    }
                }
            }
            .addOnFailureListener { e->
                Log.d("Error in Product List",e.toString())
            }
    }
    //Get product details from activity-----------------
    fun getProductDetails(activity: Activity,productId:String){
        mFireStore.collection(Constants.PRODUCTS)
            .document(productId)
            .get()
            .addOnSuccessListener { document->
                val product=document.toObject(Product::class.java)
                if (product!=null){
                    when(activity){
                        is ProductSettingActivity ->{
                            activity.getProductDetailsSuccess(product)
                        }

                        is ProductScreenActivity ->{
                            activity.getProductDetailsSuccess(product)
                        }
                        is CartActivity->{
                            activity.getProductDetailSuccess(product)
                        }
                    }
                }
            }
            .addOnFailureListener { e->
                Log.d("Error Product Details",e.toString())
            }
    }



    //Update the Product in the database-------
    fun updateProductDetails(activity: Activity,productId:String,hashmap:HashMap<String,Any>){
        mFireStore.collection(Constants.PRODUCTS)
            .document(productId)
            .update(hashmap)
            .addOnSuccessListener { document->
                when(activity){
                    is ProductSettingActivity ->{
                        activity.productUpdatedSuccess()
                    }
                }
            }
    }

    //Deleting the Product ------------
    fun deleteProduct(activity: MySellProduct, productId:String){
        mFireStore.collection(Constants.PRODUCTS)
            .document(productId)
            .delete()
            .addOnSuccessListener {
                activity.deleteProductSuccess()
            }
            .addOnFailureListener {e->
                //TODO("Hide progress dialog")
                Log.e(activity.javaClass.simpleName,"error while deleting Product",e)
            }
    }


    //-------------------------------------ADDRESS---------------------------------------------------
    fun addAddressToFireStore(activity: Activity,address: Address){
        mFireStore.collection(Constants.USER_ADDRESS)
            .add(address)
            .addOnSuccessListener { document->
                val addressId=document.id

                //Updating address ID in database
                mFireStore.collection(Constants.USER_ADDRESS)
                    .document(addressId)
                    .update(Constants.ADDRESS_ID,addressId)
                    .addOnSuccessListener {
                        when(activity){
                            is AddAddressActivity ->{
                                activity.addAddressToFireStoreSuccess(addressId)
                            }
                        }
                    }
                    .addOnFailureListener { e->
                        Log.d("Error while updating Address Id",e.toString())
                    }
            }
            .addOnFailureListener {e->
                Log.d("Error while updating Address",e.toString())

            }
    }

    fun getAddressOfUser(activity: Activity,userId:String){
        mFireStore.collection(Constants.USER_ADDRESS)
            .whereEqualTo(Constants.ADDRESS_USER_ID,userId)
            .get()
            .addOnSuccessListener {document->
                val addressList:ArrayList<Address> =ArrayList()

                for (i in document.documents){
                    val address=i.toObject(Address::class.java)
                    addressList.add(address!!)
                }

                when(activity){

                    is MyAddressActivity ->{
                        activity.getUserAddressSuccess(addressList)
                    }
                }
            }
            .addOnFailureListener { e->
                Log.d("error while getting Address List",e.toString())
            }

    }



    //-------------------------------------------Orders-----------------------------------------------

    //save Order to fireBase Storage------------------------
    fun setOrderDetails(activity: UPIPaymentActivity, order: Order){
        mFireStore.collection(Constants.ORDERS)
            .add(order)
            .addOnSuccessListener { document->

                //adding OrderId to the database
                val orderId=document.id
                mFireStore.collection(Constants.ORDERS)
                    .document(orderId)
                    .update("id",orderId)
                    .addOnSuccessListener {
                        when(activity){
                            is UPIPaymentActivity ->{
                                if (order.orderPlaced){
                                    activity.showProgressDialog("Placing Order...")
                                    activity.setOrderDetailsSuccess(orderId)
                                }
                                else{
                                    activity.showProgressDialog("Order UnSuccessful...")
                                    activity.setOrderDetailsUnSuccessful(orderId)
                                }

                            }
                    }

                }
                    .addOnFailureListener { e->
                        Log.d("Error in Storing Order",e.toString())
                        activity.hideProgressDialog()
                        activity.showErrorSnackBar("error in storing Order",true)
                    }
            }
    }

    fun getOrderDetails(activity: Activity,orderId:String){
        mFireStore.collection(Constants.ORDERS)
            .document(orderId)
            .get()
            .addOnSuccessListener { document->
                val order: Order =document.toObject(Order::class.java)!!
                when(activity){
                    is OrderPlacedActivity ->{
                        activity.getOrderDetailsSuccess(order)
                    }
                }
            }
            .addOnFailureListener { e->
                Log.d("Error while Getting Order Details",e.toString())
            }
    }



    // get order List---
    fun getOrderListForUser(activity: Activity,userId:String){
        mFireStore.collection(Constants.ORDERS)
            .whereEqualTo(Constants.USERID,userId)
            .get()
            .addOnSuccessListener { document->
                val orderList:ArrayList<Order> =ArrayList()

                for (i in document){
                    val order=i.toObject(Order::class.java)
                    orderList.add(order)
                }

                when(activity){
                    is MyOrdersActivity ->{
                        activity.getOrderListSuccess(orderList)
                    }
                }
            }
            .addOnFailureListener { e->
                Log.d("Error while getting Order List ",e.toString())
            }
    }

    //-----------------------------------Payments Details---------------------------------------------
    fun paymentSuccessful(activity: UPIPaymentActivity, payment: Payment){
        mFireStore.collection(Constants.PAYMENT_DETAILS)
            .document()
            .set(payment, SetOptions.merge())
            .addOnSuccessListener {
                activity.paymentSuccessfullyDone()
            }
            .addOnFailureListener { e->
                Log.d("Error in Storing transaction Details",e.toString())
            }
    }

    fun paymentFailed(activity: UPIPaymentActivity, payment: Payment){
        mFireStore.collection(Constants.PAYMENT_DETAILS)
            .document()
            .set(payment, SetOptions.merge())
            .addOnSuccessListener {
                activity.paymentFailedStatus()
            }
            .addOnFailureListener { e->
                Log.d("Error in Storing transaction Details",e.toString())
            }
    }



    //---------------------CART DATA----------------------------------------------------------

    fun addProductToCartForUser(activity: ProductScreenActivity, cartItem: CartItem){
        mFireStore.collection(Constants.CARTITEMS)
            .add(cartItem)
            .addOnSuccessListener { document->
                val cartItemId=document.id
                mFireStore.collection(Constants.CARTITEMS)
                    .document(cartItemId).update("id",cartItemId)
                    .addOnSuccessListener {
                        activity.itemAddedSuccessfully()
                    }
                    .addOnFailureListener { e->
                        Log.d("Error Adding Item To Cart",e.toString())
                    }
            }
            .addOnFailureListener {e->
                Log.d("Error Adding Item To Cart",e.toString())

            }
    }
    fun getItemFromCartForUser(activity: CartActivity, userId:String){
        mFireStore.collection(Constants.CARTITEMS)
            .whereEqualTo("userId",userId)
            .get()
            .addOnSuccessListener { document->
                val cartItemList:ArrayList<CartItem> = ArrayList()
                for (i in document.documents){
                    val cartItem=i.toObject(CartItem::class.java)!!
                    cartItemList.add(cartItem)
                }
                activity.getCartItemSuccess(cartItemList)
            }
    }


    //------------------------Account Details------------------------------------------------------
    fun storeUpiAccountDetails(activity: PaymentDetailActivity, accountDetails:AccountDetails){
        mFireStore.collection(Constants.ACCOUNTDETAILS)
            .document().set(accountDetails)
            .addOnSuccessListener {
                when(activity){
                    is PaymentDetailActivity->{
                        activity.accountDetailsSavedSuccessfully()
                    }
                }
            }
            .addOnFailureListener { e->
                Log.d("Error while Storing Account",e.toString())
            }
    }


    fun getUpiAccountDetails(activity: Activity,userID:String){
        mFireStore.collection(Constants.ACCOUNTDETAILS)
            .whereEqualTo("userId",userID)
            .get()
            .addOnSuccessListener { document->
                for (i in document){
                    val accountDetails=i.toObject(AccountDetails::class.java)
                    when(activity){
                        is UPIPaymentActivity->{
                            activity.getAccountDetailsSuccess(accountDetails)
                        }
                    }
                }

            }
            .addOnFailureListener { e->
                Log.d("Error While Getting Account Details",e.toString())
            }
    }

}