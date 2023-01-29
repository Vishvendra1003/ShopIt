package com.torrex.shopit.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.torrex.shopit.R
import com.torrex.shopit.firestore.FireStoreClass
import com.torrex.shopit.models.AccountDetails
import com.torrex.shopit.utils.Constants
import kotlinx.android.synthetic.main.activity_upipayment.*

class PaymentDetailActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upipayment)

        setControlsInactive()
        et_payment_account_user_id.setText(Constants.getCurrentProfileUser()!!.id)
        btn_edit_payment_details.setOnClickListener(this)

    }

    private fun setControlsInactive(){
        et_payment_account_upi.isEnabled=false
        et_payment_account_merchant_code.isEnabled=false
        et_payment_account_user_id.isEnabled=false
        et_payment_account_user_name.isEnabled=false
    }

    private fun setControlsActive(){
        et_payment_account_upi.isEnabled=true
        et_payment_account_merchant_code.isEnabled=true
        et_payment_account_user_name.isEnabled=true
        btn_edit_payment_details.text="Save Payment Details"
    }

    override fun onClick(view: View?) {
        if (view!=null){
            when(view.id){
                R.id.btn_edit_payment_details->{
                    if (btn_edit_payment_details.text.toString().trim { it <=' ' }=="Edit Payment Details"){
                        setControlsActive()
                    }
                    else{

                        val userId=Constants.getCurrentProfileUser()!!.id
                        val payeeVpa=et_payment_account_upi.text.toString().trim { it<=' ' }
                        val payeeName=et_payment_account_user_name.text.toString().trim { it<=' ' }
                        val payeeMerchantCode=et_payment_account_merchant_code.text.toString().trim { it<= ' ' }

                        val accountDetails=AccountDetails(
                            userId,
                            payeeVpa,
                            payeeName,
                            payeeMerchantCode,
                        )
                        showProgressDialog("Saving...")
                        FireStoreClass().storeUpiAccountDetails(this,accountDetails)
                    }
                }
            }
        }
    }

    //Account Details Added Successfully
    fun accountDetailsSavedSuccessfully(){
        val hashMap=HashMap<String,Any>()
        hashMap[Constants.SELLERACCOUNT]=true
        FireStoreClass().updateUserDetails(this,hashMap)
    }

    fun userProfileUpdateSuccess(){
        startActivity(Intent(this,DashboardActivity::class.java))
        hideProgressDialog()
        finish()
    }
}