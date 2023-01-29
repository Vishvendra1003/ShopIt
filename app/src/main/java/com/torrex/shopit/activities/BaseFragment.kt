package com.torrex.shopit.activities

import android.app.Dialog
import androidx.fragment.app.Fragment
import com.torrex.shopit.R
import kotlinx.android.synthetic.main.dialog_progress.*

open class BaseFragment : Fragment() {

    private lateinit var mProgressDialog: Dialog

    //Function to show ProgressDialog Box
    fun showProgressDialog(text:String){
        mProgressDialog= Dialog(requireActivity())

        /*set the screen content from layout resource.
        the resource will be inflated*/
        mProgressDialog.setContentView(R.layout.dialog_progress)
        mProgressDialog.tv_progress_bar.text=text
        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)

        mProgressDialog.show()

    }

    fun hideProgressDialog(){
        mProgressDialog.dismiss()
    }

    //function to check back Pressed



}