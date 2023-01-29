package com.torrex.shopit.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.widget.Toast
import com.torrex.shopit.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.dialog_progress.*

open class BaseActivity : AppCompatActivity() {

    private var doubleBackToExitPressedOnce=false
    private lateinit var mProgressDialog:Dialog

    //Function to Error SnackBar
    fun showErrorSnackBar(message:String,errorMessage:Boolean){
        val snackbar=Snackbar.make(findViewById(android.R.id.content),message,Snackbar.LENGTH_LONG)
        val snackbarView=snackbar.view

        if (errorMessage){
            snackbarView.setBackgroundColor(resources.getColor(R.color.red))
        }
        else{
            snackbarView.setBackgroundColor(resources.getColor(R.color.green))
        }
        snackbar.show()
    }

    //Function to show ProgressDialog Box
    fun showProgressDialog(text:String){
        mProgressDialog= Dialog(this)

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
    fun doubleBackToExit(){
        if (doubleBackToExitPressedOnce){
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce=true
        Toast.makeText(this,resources.getString(R.string.please_click_back_again_to_exit),Toast.LENGTH_SHORT).show()

        @Suppress("DEPRECATION")
        Handler().postDelayed({doubleBackToExitPressedOnce=false},2000)
    }


}