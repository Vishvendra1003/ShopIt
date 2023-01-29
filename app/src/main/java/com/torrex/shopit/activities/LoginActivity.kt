package com.torrex.shopit.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.torrex.shopit.R
import com.torrex.shopit.firestore.FireStoreClass
import com.torrex.shopit.models.User
import com.torrex.shopit.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_login.setOnClickListener(this)
        tv_sign_up.setOnClickListener(this)

    }

    override fun onClick(view: View?) {

        if (view!=null)
        {
            when(view.id){
                R.id.btn_login->{
                    if (validateDetails()){
                        showProgressDialog("Login!!")
                        loginRegisteredUser()
                    }
                    Toast.makeText(this,"Login Clicked",Toast.LENGTH_SHORT).show()
                }
                R.id.tv_sign_up->{
                    startActivity(Intent(this, RegisterNewUser::class.java))
                    Toast.makeText(this,"sign up clicked",Toast.LENGTH_SHORT).show()

                }
            }
        }
    }

    private fun loginRegisteredUser(){
        val email=et_login_email.text.toString().trim { it<=' '}
        val password=et_login_password.text.toString().trim{it<=' '}

        //Log in user from the firebaseAuth
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
            .addOnCompleteListener{task->
                if (task.isSuccessful){
                    FireStoreClass().getUserDetails(this)
                }
            }
    }

    private fun validateDetails():Boolean{
        return when{
            TextUtils.isEmpty(et_login_email.text.toString().trim { it <= ' ' }) ->{
                //TODO("error sneakbar")
                Toast.makeText(this,"enter email",Toast.LENGTH_LONG).show()
                false
            }
            TextUtils.isEmpty(et_login_password.text.toString().trim { it<=' ' })->{
                //TODO("error sneakbar")
                Toast.makeText(this,"enter password",Toast.LENGTH_LONG).show()
                false
            }
            else->{
                true
            }
        }
    }


    fun userLoggedInSuccess(user: User){
        //TODO("check if profile is completed")

        if (user.profileCompleted==1){
            hideProgressDialog()
            val intent=Intent(this, DashboardActivity::class.java)
            intent.putExtra(Constants.PROFILE_USER_DETAILS,user)
            startActivity(intent)
        }
        else{
            hideProgressDialog()
            val intent=Intent(this, ProfileActivity::class.java)
            intent.putExtra(Constants.PROFILE_USER_DETAILS,user)
            startActivity(intent)
        }

        finish()
        Toast.makeText(this,"User Logged in successfully",Toast.LENGTH_LONG).show()
    }
}