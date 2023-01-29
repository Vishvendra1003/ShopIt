package com.torrex.shopit.activities.ui.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.torrex.shopit.R
import com.torrex.shopit.activities.BaseFragment
import com.torrex.shopit.databinding.FragmentAccountBinding
import com.torrex.shopit.activities.MyOrdersActivity
import com.torrex.shopit.activities.MySellProduct
import com.torrex.shopit.activities.PaymentDetailActivity
import com.torrex.shopit.firestore.FireStoreClass
import com.torrex.shopit.utils.Constants
import kotlinx.android.synthetic.main.fragment_account.view.*

class AccountFragment : BaseFragment(), View.OnClickListener {

    private var _binding: FragmentAccountBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val view: View = binding.root

        view.tv_my_selling_products.setOnClickListener(this)
        view.tv_my_orders_account.setOnClickListener(this)
        view.tv_my_account_account.setOnClickListener(this)
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(view: View?) {
        //TODO("Not yet implemented")
        if(view!=null){
            when(view.id) {
                R.id.tv_my_selling_products -> {
                    if (Constants.getCurrentProfileUser()!!.sellerAccount){
                        startActivity(Intent(activity, MySellProduct::class.java))
                    }
                    else{

                        startActivity(Intent(activity, PaymentDetailActivity::class.java))
                    }

                }
                R.id.tv_my_orders_account->{
                    startActivity(Intent(activity, MyOrdersActivity::class.java))
                }
                R.id.tv_my_account_account->{
                    startActivity(Intent(activity, PaymentDetailActivity::class.java))

                }
            }
        }

    }
}