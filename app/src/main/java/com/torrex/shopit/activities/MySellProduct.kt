package com.torrex.shopit.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.torrex.shopit.R
import com.torrex.shopit.adapters.ProductListAdapter
import com.torrex.shopit.firestore.FireStoreClass
import com.torrex.shopit.models.Product
import kotlinx.android.synthetic.main.activity_my_sell_product.*

class MySellProduct : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_sell_product)

        setUpActionBar()

        fb_product_add_list.setOnClickListener(this)

        Toast.makeText(this,"My product Screen",Toast.LENGTH_SHORT).show()


    }

    private fun setUpActionBar(){
        setSupportActionBar(toolbar_my_sell_product_list)

        val actionBar=supportActionBar
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        }
        toolbar_my_sell_product_list.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onClick(view: View?) {
        if (view!=null){
            when(view.id){
                R.id.fb_product_add_list->{
                    startActivity(Intent(this, AddProductActivity::class.java))
                }
            }
        }
    }


    //Get product List-------------------------------------
    private fun getProductListFromFireStore(activity: MySellProduct){
        //TODO("Show Progress bar")
        showProgressDialog("")
        FireStoreClass().getProductList(this)
    }

    //Success-----
    fun getProductListSuccess(productList:ArrayList<Product>){
        //TODO("hide progress bar")
        hideProgressDialog()
        if (productList.size>0){
            tv_no_sell_product_list.visibility=View.GONE
            rv_my_sell_product_list.visibility=View.VISIBLE

            rv_my_sell_product_list.layoutManager=LinearLayoutManager(this)
            rv_my_sell_product_list.setHasFixedSize(true)

            val adapter= ProductListAdapter(this,productList)
            rv_my_sell_product_list.adapter=adapter
        }
        else{
            tv_no_sell_product_list.visibility=View.VISIBLE
            rv_my_sell_product_list.visibility=View.GONE
        }
    }

    //fun deleteProduct
    fun deleteProduct(activity: MySellProduct, productId:String) {

        //show dialog for YES AND NO"-----------
        val builder = AlertDialog.Builder(this)
        //Set title and msg for dialog
        builder.setTitle(resources.getString(R.string.delete_dialog))
        builder.setMessage(resources.getString(R.string.dialog_delete_confirm))

        //Performing positive action
        builder.setPositiveButton("YES") { dialogBox, _ ->
            //TODO("Show progress bar")
            showProgressDialog("Deleting...")
            FireStoreClass().deleteProduct(this,productId)
            dialogBox.dismiss()
        }
        //Performing Negative Action--
        builder.setNegativeButton("NO") { dialogBox, _ ->
            dialogBox.dismiss()
        }
        val alertDialogBox: AlertDialog = builder.create()
        alertDialogBox.setCancelable(false)
        alertDialogBox.show()
    }


    //Success in Deleting the Product
    fun deleteProductSuccess(){
        hideProgressDialog()
        getProductListFromFireStore(this)
    }


    //------------------------On Resume-------------------------------
    override fun onResume() {
        getProductListFromFireStore(this)
        super.onResume()
    }
}