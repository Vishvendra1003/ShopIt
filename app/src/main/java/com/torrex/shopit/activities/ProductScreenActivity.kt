package com.torrex.shopit.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.torrex.shopit.R
import com.torrex.shopit.adapters.SlideImageAdapter
import com.torrex.shopit.firestore.FireStoreClass
import com.torrex.shopit.models.CartItem
import com.torrex.shopit.models.Product
import com.torrex.shopit.utils.Constants
import com.torrex.shopit.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_product_screen.*

class ProductScreenActivity : BaseActivity(), View.OnClickListener {

    private var mProductId:String?=null
    private var mProduct: Product = Product()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_screen)

        setUpActionBar()

        if (intent.hasExtra(Constants.EXTRA_PRODUCT_ID)){
            mProductId= intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)
            //loadProductDetails()
        }

        btn_product_screen_add_to_cart.setOnClickListener(this)
        btn_product_screen_buy_now.setOnClickListener(this)

    }

    //function for loading the product details-----------
    private fun loadProductDetails(){
        if (mProductId!=null){
            FireStoreClass().getProductDetails(this, mProductId!!)
        }
    }

    //product Details Success
    fun getProductDetailsSuccess(product: Product){
        mProduct=product

        if (product.productList.size>0){
            val adapter=SlideImageAdapter(this,product.productList)
            vp_product_screen_image.adapter=adapter
        }

        tv_product_screen_title.text=product.productName
        tv_product_screen_price.text=product.productPrice.toString()
        tv_product_screen_description.text=product.productDescription
        val productQuantity=product.productQuantity
        if (productQuantity>0){
            tv_product_screen_quantity.text=productQuantity.toString()
        }
        else{
            tv_product_screen_quantity.text="Out Of Stock"
            tv_product_screen_quantity.setTextColor(resources.getColor(R.color.red))
        }

    }




    //Toolbar setup------------------
    private fun setUpActionBar(){
        setSupportActionBar(toolbar_product_screen_activity)
        val actionBar=supportActionBar

        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        }
        toolbar_product_screen_activity.setNavigationOnClickListener { onBackPressed() }
    }

    //OnResume Function----------------------------
    override fun onResume() {
        loadProductDetails()
        super.onResume()

    }

    override fun onClick(view: View?) {

        if (view!=null){
            when(view.id){
                R.id.btn_product_screen_add_to_cart->{
                    if (mProduct.productQuantity>0){
                        addToCart()
                    }
                    else{
                        showErrorSnackBar("Sorry Product Out Of Stock!!!",true)
                    }
                }

                R.id.btn_product_screen_buy_now->{
                    if (mProduct.productQuantity>0){
                        val intent=Intent(this, MyAddressActivity::class.java)
                        intent.putExtra(Constants.ORDER_SUMMARY_PRODUCT,mProduct)
                        startActivity(intent)
                    }
                    else{
                        showErrorSnackBar("Sorry Product Out Of Stock!!!",true)
                    }


                }
            }
        }

    }

    private fun addToCart(){
        showProgressDialog("Adding Product To Cart")
        val userId= FireStoreClass().getCurrentUserID()
        val cartItem: CartItem = CartItem(
            userId,
            mProduct.userId,
            mProductId!!,
            mProduct.productName,
            mProduct.productPrice,
            mProduct.productImage,
            mProduct.productQuantity,
            mProduct.productQuantity,
        )

        FireStoreClass().addProductToCartForUser(this,cartItem)
    }

    fun itemAddedSuccessfully(){
        hideProgressDialog()
        btn_product_screen_add_to_cart.isEnabled=false
        btn_product_screen_add_to_cart.setText(resources.getString(R.string.btn_product_screen_added_to_cart))
        btn_product_screen_add_to_cart.setTextColor(resources.getColor(R.color.red))


    }

}
