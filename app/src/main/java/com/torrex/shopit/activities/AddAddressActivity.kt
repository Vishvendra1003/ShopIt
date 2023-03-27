package com.torrex.shopit.activities

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.torrex.shopit.R
import com.torrex.shopit.firestore.FireStoreClass
import com.torrex.shopit.models.Address
import kotlinx.android.synthetic.main.activity_add_address.*

class AddAddressActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_address)

        btn_address_save.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        if (view!=null){
            when(view.id){
                R.id.btn_address_save->{
                    if (validateAddressDetails()){
                        addAddressToFireStore()
                    }
                }
            }
        }
    }

    //Validate all Address Details-------------------------------------------------------------------
    private fun validateAddressDetails():Boolean{
        return when {
            TextUtils.isEmpty(et_address_line_1.text.toString().trim {it <= ' ' }) -> {
                showErrorSnackBar("Please enter address line 1",true)
                false
            }
            TextUtils.isEmpty(et_address_line_2.text.toString().trim {it <= ' ' }) -> {
                showErrorSnackBar("Please your enter address line 2",true)
                false
            }
            TextUtils.isEmpty(et_address_line_3.text.toString().trim {it <= ' ' }) -> {
                showErrorSnackBar("Please enter address line 3",true)
                false
            }
            TextUtils.isEmpty(et_address_city.text.toString().trim {it <= ' ' }) -> {
                showErrorSnackBar("Please enter City",true)
                false
            }
            TextUtils.isEmpty(et_address_state.text.toString().trim {it <= ' ' }) -> {
                showErrorSnackBar("Please enter State",true)
                false
            }
            TextUtils.isEmpty(et_address_postal_code.text.toString().trim {it <= ' ' }) -> {
                showErrorSnackBar("Please enter Postal Code",true)
                false
            }
            et_address_postal_code.text.toString().trim {it <= ' ' }.length!=6->{
                showErrorSnackBar("Please enter Valid 6 digit Postal Code",true)
                false
            }
            et_address_mobile_no.text.toString().trim {it <= ' ' }.length!=10->{
                showErrorSnackBar("Please enter Valid 10 Mob NO.",true)
                false
            }

            else->{
                true
            }
        }
    }

    //Save Address to the FireStore database---------------------------------------------------------
    private fun addAddressToFireStore(){
        showProgressDialog("please wait")
        val currentUser= FireStoreClass().getCurrentUserID()
        val mAddress= Address(
            currentUser,
            et_address_line_1.text.toString().trim { it<= ' ' },
            et_address_line_2.text.toString().trim { it<= ' ' },
            et_address_line_3.text.toString().trim { it<= ' ' },
            et_address_city.text.toString().trim { it<= ' ' },
            et_address_state.text.toString().trim { it<= ' ' },
            et_address_postal_code.text.toString().trim { it<= ' ' }.toInt(),
            et_address_mobile_no.text.toString().trim { it<= ' ' }.toLong(),
            )

        FireStoreClass().addAddressToFireStore(this,mAddress)
    }

    //Add Address Success--------
    fun addAddressToFireStoreSuccess(addressId:String){
        hideProgressDialog()
        showErrorSnackBar("Address Added Success",false)
        finish()
    }
}