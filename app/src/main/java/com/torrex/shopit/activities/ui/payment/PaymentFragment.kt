package com.torrex.shopit.activities.ui.payment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.torrex.shopit.R
import com.torrex.shopit.activities.BarCodeScannerActivity
import com.torrex.shopit.activities.UPIPaymentActivity
import kotlinx.android.synthetic.main.activity_upipayment.*
import kotlinx.android.synthetic.main.fragment_payment.*
import kotlinx.android.synthetic.main.fragment_payment.view.*


class PaymentFragment : Fragment(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_payment, container, false)

        //first create view than by view add onclick listener
        view.btn_continue_payment.setOnClickListener(this)
        return view
    }

    override fun onClick(view: View?) {

        if (view!=null){
            when(view.id){
                R.id.btn_continue_payment->{
                    startActivity(Intent(activity, BarCodeScannerActivity::class.java))
                }
            }
        }

    }



}