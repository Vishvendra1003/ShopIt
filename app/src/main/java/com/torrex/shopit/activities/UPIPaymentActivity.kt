package com.torrex.shopit.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.torrex.shopit.R
import com.torrex.shopit.firestore.FireStoreClass
import com.torrex.shopit.models.AccountDetails
import com.torrex.shopit.models.Order
import com.torrex.shopit.models.Payment
import com.torrex.shopit.utils.Constants
import dev.shreyaspatil.easyupipayment.EasyUpiPayment
import dev.shreyaspatil.easyupipayment.listener.PaymentStatusListener
import dev.shreyaspatil.easyupipayment.model.TransactionDetails
import dev.shreyaspatil.easyupipayment.model.TransactionStatus
import kotlinx.android.synthetic.main.activity_upipayment.*
import kotlinx.android.synthetic.main.payment_summary.*

class UPIPaymentActivity : BaseActivity(), PaymentStatusListener {

    private var order: Order = Order()
    private var paymentDetails: Payment = Payment()
    private var paymentTransactionId:String=""
    private var mAccountDetails: AccountDetails = AccountDetails()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.payment_summary)


        //Intent extra information------------------------------------------------
        if (intent.hasExtra(Constants.EXTRA_ORDER_DETAILS_PAYMENT)){
            order=intent.getParcelableExtra<Order>(Constants.EXTRA_ORDER_DETAILS_PAYMENT)!!
            val productOwner=order.product.userId
            FireStoreClass().getUpiAccountDetails(this,productOwner)
        }


        btn_make_payment.setOnClickListener(){
            easyUpiPayment()
        }
    }


    private fun easyUpiPayment(){

        //showing Progress Bar
        showProgressDialog("Making Payment...")

        //Details for the easyPay Data
            val amount=tv_payment_summary_amount.text.toString().trim { it<=' ' }
            val userId=tv_payment_summary_user_id_name.text.toString().trim { it<=' ' }
            val upi=tv_payment_summary_upi.text.toString().trim { it<=' ' }
            val description=tv_payment_summary_description.text.toString().trim { it<=' ' }
            val payeeMerchantCode= mAccountDetails.payeeMerchantCode
            paymentTransactionId=System.currentTimeMillis().toString()
            println(paymentTransactionId)

        //Storing Payment Details in the Database
        //TODO("Change this code to after the transaction is done successfully")
        order.orderPaymentTransactionId=paymentTransactionId
        paymentDetails= Payment(upi,userId,paymentTransactionId,paymentTransactionId,amount,payeeMerchantCode,description)

        //Starting Payment for the Order----------------------------------------------------------
            val easyUpiPayment= EasyUpiPayment(this){
                this.payeeVpa=upi
                this.payeeName=userId
                this.transactionId= paymentTransactionId
                this.transactionRefId=paymentTransactionId
                this.amount=amount
                this.payeeMerchantCode=payeeMerchantCode
                this.description=description

            }

            easyUpiPayment.startPayment()
            easyUpiPayment.setPaymentStatusListener(this)
    }

    override fun onTransactionCancelled() {
        Toast.makeText(this,"Cancelled by user",Toast.LENGTH_SHORT).show()
        TODO("Not yet implemented")
    }

    override fun onTransactionCompleted(transactionDetails: TransactionDetails) {
        Log.d("TransactionDetails", transactionDetails.toString())
        if (transactionDetails.transactionStatus==TransactionStatus.FAILURE){
            paymentDetails.paymentStatus="failed"
            showErrorSnackBar("Payment Failed",true)
            FireStoreClass().paymentFailed(this,paymentDetails)
        }
        if (transactionDetails.transactionStatus==TransactionStatus.SUCCESS){
            paymentDetails.paymentStatus="Success"
            FireStoreClass().paymentSuccessful(this,paymentDetails)

        }

    }



    //Setting View for the Payment Details
    private fun setPaymentDetails(order: Order){
        tv_payment_summary_upi.text= mAccountDetails.payeeVpa
        tv_payment_summary_description.text= mAccountDetails.userId
        tv_payment_summary_amount.text=(order.totalAmount+0.00).toString()
        tv_payment_summary_user_id_name.text=mAccountDetails.userId
    }


    //Transaction done Successfully-----------------------
    fun paymentSuccessfullyDone(){
        hideProgressDialog()
        order.orderPlaced=true
        FireStoreClass().setOrderDetails(this,order)
    }

    //transaction failed------------------
    fun paymentFailedStatus(){
        hideProgressDialog()
        FireStoreClass().setOrderDetails(this,order)
    }
    //Order Placed Successfully---------------------------------------
    fun setOrderDetailsSuccess(orderId:String){
        //TODO("Order Success Screen")
        hideProgressDialog()
        val hashMap=HashMap<String,Any>()
        hashMap[Constants.PRODUCTQUANTITY]=order.product.productQuantity-(order.productQuantity)
        FireStoreClass().updateProductDetails(this,order.product.productId,hashMap)
        showErrorSnackBar("Order Placed Successfully",false)

        val intent=Intent(this, OrderPlacedActivity::class.java)
        intent.putExtra(Constants.ORDER_PLACED_INVOICE,orderId)
        startActivity(intent)
        finish()
    }

    fun setOrderDetailsUnSuccessful(orderId: String){
        hideProgressDialog()

        //TODO("Order Success Screen")
        val hashMap=HashMap<String,Any>()
        hashMap[Constants.PRODUCTQUANTITY]=order.product.productQuantity-(order.productQuantity)
        FireStoreClass().updateProductDetails(this,order.product.productId,hashMap)
        //TODO("Order Success Screen")

        showErrorSnackBar("Order Not Placed",false)
        val intent=Intent(this, OrderPlacedActivity::class.java)
        intent.putExtra(Constants.ORDER_PLACED_INVOICE,orderId)
        startActivity(intent)
        finish()
    }


    //Account Details For the Payment
    fun getAccountDetailsSuccess(accountDetails:AccountDetails){
        if (accountDetails!=null){
            mAccountDetails=accountDetails
            setPaymentDetails(order)
        }

    }
}