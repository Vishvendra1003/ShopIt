package com.torrex.shopit.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Adapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.torrex.shopit.R
import com.torrex.shopit.adapters.ProductListAdapter
import com.torrex.shopit.adapters.SlideImageAdapter
import com.torrex.shopit.firestore.FireStoreClass
import com.torrex.shopit.models.Product
import com.torrex.shopit.utils.Constants
import com.torrex.shopit.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_product_setting.*

class ProductSettingActivity : BaseActivity(), View.OnClickListener {
    var mProduct:Product=Product()
    var productId:String=""
    var productUserId:String=""
    var mSelectedProductImage:Uri?=null
    var mAddProductImage:Uri?=null
    var mProductImage:String=""
    var mAddProductImageList:String=""
    var mImageEdit:String=""


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_setting)

        setUpActionBar()

        setControlsDisable()

        if (intent.hasExtra(Constants.EXTRA_PRODUCT_ID)){
            productId= intent.getStringExtra(Constants.EXTRA_PRODUCT_ID).toString()
            productUserId=intent.getStringExtra(Constants.EXTRA_PRODUCT_USER_ID).toString()

            getProductDetailsFromFireStore()
        }

        btn_edit_product_edit.setOnClickListener(this)
        iv_edit_product_image_edit.setOnClickListener(this)
        iv_add_product_image_list.setOnClickListener(this)

    }
    private fun getProductDetailsFromFireStore() {
        FireStoreClass().getProductDetails(this,productId )
    }


    //Disabling the controls
    private fun setControlsDisable() {
        iv_edit_product_image_edit.isEnabled=false
        et_product_title_edit.isEnabled=false
        et_product_price_edit.isEnabled=false
        et_product_quantity_edit.isEnabled=false
        et_product_description_edit.isEnabled=false
        iv_add_product_image_list.isEnabled=false
    }

    private fun setControlsEnable() {
        iv_edit_product_image_edit.isEnabled=true
        et_product_title_edit.isEnabled=true
        et_product_price_edit.isEnabled=true
        et_product_quantity_edit.isEnabled=true
        et_product_description_edit.isEnabled=true
        iv_add_product_image_list.isEnabled=true

        btn_edit_product_edit.text= Constants.BTN_SAVE
    }


    private fun setUpActionBar(){
        setSupportActionBar(toolbar_product_setting_activity)

        val actionBar=supportActionBar
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        }
        toolbar_product_setting_activity.setNavigationOnClickListener { onBackPressed() }
    }

    fun getProductDetailsSuccess(product:Product){
        mProduct=product
        GlideLoader(this).loadUserPicture(product.productImage,iv_product_image_edit)
        et_product_title_edit.setText(product.productName)
        et_product_price_edit.setText(product.productPrice.toString())
        et_product_quantity_edit.setText(product.productQuantity.toString())
        et_product_description_edit.setText(product.productDescription)
        //loading images in viewPager---
        if(product.productList.size>0){
            val viewPagerAdapter=SlideImageAdapter(this,product.productList)
            vp_product_image_list_add_product.adapter=viewPagerAdapter
        }

    }

    override fun onClick(view: View?) {
        if (view!=null){
            when(view.id){
                R.id.btn_edit_product_edit->{
                    showErrorSnackBar("Please Update the Product!!",false)
                    if (btn_edit_product_edit.text.toString().trim{it<=' '}== Constants.BTN_EDIT){
                        setControlsEnable()
                    }
                    else{
                        //TODO("show progress bar")
                        if (validateProductDetails()){
                            showProgressDialog("Updating Product....")
                            Toast.makeText(this,"Submit Clicked",Toast.LENGTH_SHORT).show()

                            if (mSelectedProductImage!=null) {
                                FireStoreClass().uploadImageToCloudStorage(this, mSelectedProductImage!!,
                                    Constants.PRODUCT_IMAGE)
                            }
                            else{
                                updateProductDetails()
                            }
                        }



                    }
                }

                R.id.iv_edit_product_image_edit->{
                    mImageEdit=Constants.IMAGE_EDIT
                    Constants.showImageChooser(this)
                }
                R.id.iv_add_product_image_list->{
                    mImageEdit=Constants.IMAGE_ADD
                    Constants.showImageChooser(this)
                }
            }
        }
    }


    //Validating the details------
    private fun validateProductDetails():Boolean{
        return when{
            TextUtils.isEmpty(et_product_title_edit.text.toString().trim { it<=' ' })->{
                //TODO("validation error")
                false
            }
            TextUtils.isEmpty(et_product_price_edit.text.toString().trim { it<=' ' })->{
                //TODO("validation error")
                false
            }
            TextUtils.isEmpty(et_product_description_edit.text.toString().trim { it<=' ' })->{
                //TODO("validation error")
                false
            }
            TextUtils.isEmpty(et_product_quantity_edit.text.toString().trim { it<=' ' })->{
                //TODO("validation error")
                false
            }
            else->{
                true
            }
        }

    }


    //Image upload Success-----------------
    fun uploadProductImageSuccess(url:String){
        if (mImageEdit==Constants.IMAGE_EDIT){
            mProductImage=url
        }
        if (mImageEdit==Constants.IMAGE_ADD){
            mAddProductImageList=url
        }
        updateProductDetails()
    }

    fun updateProductDetails(){
        val hashMap= HashMap<String,Any>()
        val productImageList=mProduct.productList
        productImageList.add(mAddProductImageList)

        if (mSelectedProductImage!=null){
            hashMap[Constants.PRODUCTIMAGE]=mProductImage
        }
        if (mAddProductImage!=null){
            hashMap[Constants.PRODUCTLIST]=productImageList
        }
        val mProductName=et_product_title_edit.text.toString().trim { it<=' ' }
        val mProductPrice=et_product_price_edit.text.toString().trim { it<=' ' }.toLong()
        val mProductQuantity=et_product_quantity_edit.text.toString().trim { it<=' ' }.toInt()
        val mProductDescription=et_product_description_edit.text.toString().trim { it<=' ' }

        hashMap[Constants.PRODUCTNAME]=mProductName
        hashMap[Constants.PRODUCTPRICE]=mProductPrice
        hashMap[Constants.PRODUCTQUANTITY]=mProductQuantity
        hashMap[Constants.PRODUCTDESCRIPTION]=mProductDescription

        FireStoreClass().updateProductDetails(this,productId,hashMap)
    }

    //ProductUpdated Success----------------
    fun productUpdatedSuccess(){
        //TODO("Hide Progress bar")
        hideProgressDialog()
        finish()
    }

    //Over write function for the for Activity result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode==Activity.RESULT_OK){
            if (requestCode== Constants.PICK_IMAGE_REQUEST_CODE){

                if (mImageEdit==Constants.IMAGE_EDIT){
                    mSelectedProductImage=data!!.data
                    GlideLoader(this).loadUserPicture(mSelectedProductImage!!,iv_product_image_edit)
                }
                if (mImageEdit==Constants.IMAGE_ADD){
                    showProgressDialog("Adding Picture")
                    mAddProductImage=data!!.data

                    FireStoreClass().uploadImageToCloudStorage(this,mAddProductImage!!,Constants.PRODUCT_IMAGE)


                }

            }


        }
    }
}