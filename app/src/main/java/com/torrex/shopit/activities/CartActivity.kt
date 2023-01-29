package com.torrex.shopit.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.torrex.shopit.R
import com.torrex.shopit.adapters.CartItemListAdapter
import com.torrex.shopit.firestore.FireStoreClass
import com.torrex.shopit.models.CartItem
import com.torrex.shopit.models.Product
import com.torrex.shopit.utils.Constants
import kotlinx.android.synthetic.main.activity_cart.*

class CartActivity : AppCompatActivity() {
    private var mProductSelected:Product= Product()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        setUpActionBar()

    }


    //Toolbar setup------------------
    private fun setUpActionBar(){
        setSupportActionBar(toolbar_cart_list)
        val actionBar=supportActionBar

        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        }
        toolbar_cart_list.setNavigationOnClickListener { onBackPressed() }
    }



    //Getting Item from cart
    private fun getItemFromCartForUser(currentUser:String){
        FireStoreClass().getItemFromCartForUser(this, currentUser)
    }

    //GetCart Item Success
    fun getCartItemSuccess(cartItemList:ArrayList<CartItem>){
        if (cartItemList.size>0){
            tv_no_item_in_cart_list.visibility= View.GONE
            rv_cart_list.visibility= View.VISIBLE

            rv_cart_list.layoutManager= LinearLayoutManager(this)
            rv_cart_list.setHasFixedSize(true)

            val adapter= CartItemListAdapter(this,cartItemList)

            adapter.setOnClickListener(object: CartItemListAdapter.OnClickListener{
                override fun onClick(position: Int, cartItem: CartItem) {
                    val productId=cartItem.productId
                    FireStoreClass().getProductDetails(this@CartActivity,productId)
                }
            })

            rv_cart_list.adapter=adapter
        }
    }


    //Get Product Details Success--------------------------
    fun getProductDetailSuccess(product:Product){
        mProductSelected=product
        val intent=Intent(this@CartActivity,MyAddressActivity::class.java)
        intent.putExtra(Constants.ORDER_SUMMARY_PRODUCT,mProductSelected)
        startActivity(intent)
    }

    //OnResume----------------------
    override fun onResume() {
        getItemFromCartForUser(Constants.currentLoggedInUser)
        super.onResume()
    }
}