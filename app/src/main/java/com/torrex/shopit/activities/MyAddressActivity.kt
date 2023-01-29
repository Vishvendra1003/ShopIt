package com.torrex.shopit.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.torrex.shopit.R
import com.torrex.shopit.adapters.AddressListAdapter
import com.torrex.shopit.firestore.FireStoreClass
import com.torrex.shopit.models.Address
import com.torrex.shopit.models.Product
import com.torrex.shopit.utils.Constants
import kotlinx.android.synthetic.main.activity_my_address.*

class MyAddressActivity : BaseActivity() {

    var mCurrentUser:String=""
    var mProductSelected: Product = Product()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_address)

        //Extra Information------------------
        if (intent.hasExtra(Constants.ORDER_SUMMARY_PRODUCT)){
            mProductSelected=intent.getParcelableExtra<Product>(Constants.ORDER_SUMMARY_PRODUCT)!!
        }

        //floating button on click listener--------------------------------------------------
        fb_my_address_add.setOnClickListener{
            startActivity(Intent(this, AddAddressActivity::class.java))
        }

    }

    //fun for getting all addresses-----------------------------------------------------------------
    private fun getAddressesFromFireStore(){
        showProgressDialog("Loading...")
        mCurrentUser= FireStoreClass().getCurrentUserID()
        FireStoreClass().getAddressOfUser(this,mCurrentUser)
    }

    //function for getAddressSuccess----------------------------------------------------------------
    fun getUserAddressSuccess(addressList:ArrayList<Address>){
        hideProgressDialog()
        if (addressList.size>0){
            tv_my_address_no_address.visibility=View.GONE
            rv_my_addresses.visibility=View.VISIBLE

            rv_my_addresses.layoutManager=LinearLayoutManager(this)
            rv_my_addresses.setHasFixedSize(true)

            val adapter= AddressListAdapter(this, addressList)
            rv_my_addresses.adapter=adapter

            adapter.setOnClickListener(object: AddressListAdapter.OnClickListener{
                override fun onClick(position: Int, address: Address) {
                    val intent=Intent(this@MyAddressActivity, OrderSummaryActivity::class.java)
                    intent.putExtra(Constants.ORDER_SUMMARY_ADDRESS,address)
                    intent.putExtra(Constants.ORDER_SUMMARY_PRODUCT,mProductSelected)
                    intent.putExtra(Constants.ORDER_SUMMARY_USER,mCurrentUser)
                    startActivity(intent)
                }

            })
        }
        else{
            tv_my_address_no_address.visibility=View.VISIBLE
            rv_my_addresses.visibility=View.GONE
        }
    }


    //function on resume----------------------------------------------------------------------------
    override fun onResume() {
        if (mProductSelected!=null){
            getAddressesFromFireStore()
        }
        else{
            Toast.makeText(this,"Product Not Selected",Toast.LENGTH_SHORT).show()
        }
        super.onResume()
    }
}