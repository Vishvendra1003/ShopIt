package com.torrex.shopit.activities

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.torrex.shopit.R
import com.torrex.shopit.adapters.OrderListAdapter
import com.torrex.shopit.firestore.FireStoreClass
import com.torrex.shopit.models.Order
import kotlinx.android.synthetic.main.activity_my_orders.*

class MyOrdersActivity : BaseActivity() {
    private var userId:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_orders)

        setUpActionBar()

        getOrderListForTheUser()
    }

    //Get Order list for the User
    private fun getOrderListForTheUser(){
        showProgressDialog("Loading..")
        userId= FireStoreClass().getCurrentUserID()

        if (userId.isNotEmpty()){
            FireStoreClass().getOrderListForUser(this,userId)

        }
    }

    //Order List Success----------------
    fun getOrderListSuccess(orderList:ArrayList<Order>){
        hideProgressDialog()
        if (orderList.size>0){
            tv_no_order_list.visibility=View.GONE
            rv_my_order_list.visibility=View.VISIBLE

            rv_my_order_list.layoutManager=LinearLayoutManager(this)
            rv_my_order_list.setHasFixedSize(true)

            rv_my_order_list.adapter= OrderListAdapter(this,orderList)

        }
        else{
            tv_no_order_list.visibility=View.VISIBLE
            rv_my_order_list.visibility=View.GONE
        }
    }



    //setUp Action Bar--------------------------------------------------------------
    private fun setUpActionBar(){
        setSupportActionBar(toolbar_my_order_list)
        val actionBar=supportActionBar
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        }
        toolbar_my_order_list.setNavigationOnClickListener { onBackPressed() }
    }
}