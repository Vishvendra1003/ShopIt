package com.torrex.shopit.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.torrex.shopit.R
import com.torrex.shopit.models.Address
import com.torrex.shopit.models.Order
import com.torrex.shopit.models.Product
import com.torrex.shopit.utils.Constants
import com.torrex.shopit.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_order_summary.*
import java.text.SimpleDateFormat
import java.util.*

class OrderSummaryActivity : BaseActivity(), View.OnClickListener {

    private var order: Order = Order()

    private var mAddress: Address = Address()
    private var mProductSelected: Product = Product()
    private var userId:String?=""
    private var mProductQuantity:Int=1
    private var totalAmount:Long=0
    private var productCharges:Long=0
    private var shippingCharges:Int=0
    private var mProductStock:Int=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_summary)


        //Getting UserId for the Product-------------------------------------------------------------
        if (intent.hasExtra(Constants.ORDER_SUMMARY_USER)){
            userId=intent.getStringExtra(Constants.ORDER_SUMMARY_USER)
            Log.d("AddressUser",userId.toString())
        }

        //Getting Address Selected for the Product---------------------------------------------------

       if (intent.hasExtra(Constants.ORDER_SUMMARY_ADDRESS)){
            mAddress=intent.getParcelableExtra<Address>(Constants.ORDER_SUMMARY_ADDRESS)!!

            if (mAddress!=null){
                et_address_order_summmary.text="${mAddress.addressLine1} ${mAddress.addressLine2} ${mAddress.addressLine3} \n " +
                        "${mAddress.addressCity} , ${mAddress.addressState}"
                et_address_mobile_order_summary.text=(mAddress.addressMobileNO).toString()
                et_address_postal_code_order_summary.text=(mAddress.addressPostalCode).toString()

            }
        }


        //Getting the Product details for the Selected Product---------------------------------------
        if (intent.hasExtra(Constants.ORDER_SUMMARY_PRODUCT)){
            Log.d("AddressUser", Constants.ORDER_SUMMARY_PRODUCT.toString())

            mProductSelected=intent.getParcelableExtra<Product>(Constants.ORDER_SUMMARY_PRODUCT)!!

            if (mProductSelected!=null){
                tv_product_name_order_summary.text=mProductSelected.productName
                tv_product_des_order_summary.text=mProductSelected.productDescription
                tv_product_price_detail_order_summary.text=mProductSelected.productPrice.toString()
                GlideLoader(this).loadUserPicture(mProductSelected.productImage,iv_product_image_order_summary)

                tv_product_quantity_order_summary.text=mProductQuantity.toString()
            }
            calculateTotalAmount()
        }

        //set all Button to click Listener---------------------------
        ib_add_product_order_summary.setOnClickListener(this)
        ib_minus_product_order_summary.setOnClickListener(this)
        btn_order_summary_pay.setOnClickListener(this)
    }



    override fun onClick(view: View?) {
        if (view!=null){
            mProductStock=mProductSelected.productQuantity
            when(view.id){
                R.id.ib_add_product_order_summary->{
                    if (mProductStock>mProductQuantity){
                        mProductQuantity += 1
                        tv_product_quantity_order_summary.text=mProductQuantity.toString()
                        calculateTotalAmount()
                    }
                    else{
                        showErrorSnackBar("Product quantity is equal to Stock Quantity",true)
                    }
                }

                R.id.ib_minus_product_order_summary->{
                    if (mProductQuantity>=1){
                        mProductQuantity -= 1
                        tv_product_quantity_order_summary.text=mProductQuantity.toString()
                        calculateTotalAmount()
                    }
                    else{
                        showErrorSnackBar("Product quantity is 0",true)
                    }

                }

                R.id.btn_order_summary_pay->{
                    createOrder()
                }
            }
        }
    }


    private fun calculateTotalAmount():Long{
        shippingCharges= Constants.shippingCharges
        productCharges=(tv_product_price_detail_order_summary.text).toString().toLong() * mProductQuantity

        totalAmount=productCharges+shippingCharges

        et_charges_order_summary.text=productCharges.toString()
        et_shipping_charges_order_summary.text=shippingCharges.toString()
        et_total_charges_order_summary.text=totalAmount.toString()
        return totalAmount
    }

    private fun createOrder(){

        val userId=userId!!
        val product=mProductSelected
        val address=mAddress
        val title=mProductSelected.productName
        val image=mProductSelected.productImage
        val subTotalAmount=productCharges
        val shippingCharge=shippingCharges
        val totalAmount=totalAmount
        val sdf=SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        val currentDate=sdf.format(Date())
        val orderDatetime=currentDate.toString()

        order= Order(
            userId,
            product,
            address,
            title,
            image,
            mProductQuantity,
            subTotalAmount,
            shippingCharge,
            totalAmount,
            orderDatetime,
        )

        //Dialog Box For Confirming to the Payment-----------
        showAlertDialog()
    }

    //fun for the dialog box----------
    private fun showAlertDialog(){
        val builder=android.app.AlertDialog.Builder(this)
        builder.setTitle("Payment")
        builder.setMessage("Are you Sure You Want to Proceed for the Payment ?")

        //performing positive action--
        builder.setPositiveButton("Yes"){dialogInterface, _->
            val intent=Intent(this, UPIPaymentActivity::class.java)
            intent.putExtra(Constants.EXTRA_ORDER_DETAILS_PAYMENT,order)
            dialogInterface.dismiss()
            startActivity(intent)
        }

        builder.setNegativeButton("No"){ dialogInterface,  _->
            dialogInterface.dismiss()
        }
        //function for AlertDialog Box
        val alertDialogBox:AlertDialog=builder.create()
        alertDialogBox.setCancelable(false)
        alertDialogBox.show()

    }

    override fun onResume() {
        super.onResume()
    }

}