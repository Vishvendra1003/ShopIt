package com.torrex.shopit.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.torrex.shopit.R
import com.torrex.shopit.firestore.FireStoreClass
import com.torrex.shopit.models.Product
import com.torrex.shopit.utils.Constants
import com.torrex.shopit.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_add_product.*
import kotlinx.android.synthetic.main.activity_product_setting.*
import kotlinx.android.synthetic.main.product_slidable_layout_small.*

class AddProductActivity : BaseActivity(), View.OnClickListener {

    private var mSelectedProductImage: Uri?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        setUpActionBar()
        btn_add_product_submit.setOnClickListener(this)
        iv_edit_product_image.setOnClickListener(this)
    }

    private fun setUpActionBar(){
        setSupportActionBar(toolbar_add_product_activity)

        val actionBar=supportActionBar
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
        toolbar_add_product_activity.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    override fun onClick(view: View?) {
        if (view!=null){
            when(view.id){
                R.id.btn_add_product_submit->{

                    if (validateProductDetails()){
                        if (mSelectedProductImage!=null){
                            showProgressDialog("Adding Product")
                            FireStoreClass().uploadImageToCloudStorage(this,mSelectedProductImage!!,
                                Constants.PRODUCT_IMAGE)
                        }

                    }
                }

                R.id.iv_edit_product_image->{
                    Constants.showImageChooser(this)
                }
            }
        }
    }

    //Validating the product details
    private fun validateProductDetails():Boolean{
        return when{
            TextUtils.isEmpty(et_product_title.text.toString().trim { it<=' ' })->{
                //TODO("validation error")
                false
            }
            TextUtils.isEmpty(et_product_price.text.toString().trim { it<=' ' })->{
                //TODO("validation error")
                false
            }
            TextUtils.isEmpty(et_product_description.text.toString().trim { it<=' ' })->{
                //TODO("validation error")
                false
            }
            TextUtils.isEmpty(et_product_quantity.text.toString().trim { it<=' ' })->{
                //TODO("validation error")
                false
            }
            else->{
                true
            }
        }

    }


    //U[loading Product image success and uploading product
    fun uploadProductImageSuccess(productUrl:String){

        val productList:ArrayList<String> =ArrayList()
        productList.add(productUrl)
        val userId= Constants.getCurrentProfileUser()!!.id
        val productName=et_product_title.text.toString().trim { it<=' ' }
        val productPrice=et_product_price.text.toString().trim { it<=' ' }.toLong()
        val productQuantity=et_product_quantity.text.toString().trim { it<=' ' }.toInt()
        val productDescription=et_product_description.text.toString().trim { it<=' ' }

        val product= Product(
            userId,
            productName,
            productPrice,
            productQuantity,
            productDescription,
            productUrl,
            productList,
        )
        FireStoreClass().registerProduct(this,product)


    }



    //Upload product Success
    fun uploadProductSuccess(){
        //TODO("Progress Bar")
        hideProgressDialog()
        Toast.makeText(this,"Product uploaded Success........",Toast.LENGTH_SHORT).show()
        finish()
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode==Activity.RESULT_OK) {
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE) {
                mSelectedProductImage = data!!.data
                GlideLoader(this).loadUserPicture(mSelectedProductImage!!, iv_product_image)
            }
        }
        }


}