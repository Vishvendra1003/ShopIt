package com.torrex.shopit.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.torrex.shopit.R
import com.torrex.shopit.firestore.FireStoreClass
import com.torrex.shopit.models.User
import com.torrex.shopit.utils.Constants
import com.torrex.shopit.utils.CustomDatePicker
import com.torrex.shopit.utils.GlideLoader
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_register_new_user.*

class RegisterNewUser : BaseActivity(), View.OnClickListener {

    private var mSelectedImageFileUri:Uri?=null
    private var mUserProfileImageUri:String=""
    private var mProfileCompleted:Int=0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_new_user)

        setUpActionBar()

        //set Screen size
        @Suppress("DEPRECATION")
        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }

            //button on click Listener
            btn_signUp.setOnClickListener(this)
            ib_date_picker.setOnClickListener(this)
            iv_profile_register_image.setOnClickListener(this@RegisterNewUser)
        }

    

    //setting up action Bar
    private fun setUpActionBar(){
        setSupportActionBar(toolbar_register_activity)

        val actionBar=supportActionBar
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        }
        toolbar_register_activity.setNavigationOnClickListener{onBackPressed()}
    }

    //register user function()---------------------------------------------------------------------


    //Register User
    private fun registerUser(){
        if (validateUserDetails()) {
            showProgressDialog("Registering User")
            val email = et_signup_email.text.toString().trim { it <= ' ' }
            val password = et_signUp_password.text.toString().trim { it <= ' ' }
            var userGender: String = ""
            var mobNo=et_signUp_mobile.text.toString().trim { it <= ' ' }.toLong()


            if (rb_male.isChecked) {
                userGender = Constants.MALE
            }
            else {
                userGender = Constants.FEMALE
            }

            //Creating the instance and register a new user with email and password in firebase
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        //firebase user--------------
                        val firebaseUser: FirebaseUser = task.result.user!!

                        //Storing the user in the fireStore database

                        //---------------------User--------------------------------------
                        val user = User(
                            firebaseUser.uid,
                            et_signUp_first_name.text.toString().trim { it <= ' ' },
                            et_signUp_last_name.text.toString().trim { it <= ' ' },
                            et_signup_email.text.toString().trim { it <= ' ' },
                            mobNo,
                            //et_signUp_sex.text.toString().trim { it<=' ' },
                            userGender,
                            tv_signUp_dob.text.toString().trim { it <= ' ' },
                            mUserProfileImageUri,
                            mProfileCompleted
                            )

                        //--------Storing-------------------------
                        FireStoreClass().registerUser(this, user)
                        //Toast.makeText(this, ".............", Toast.LENGTH_SHORT).show()

                    }

                }

            Toast.makeText(this, "SignUP clicked.............", Toast.LENGTH_SHORT).show()
        }
    }




    //Validate the user details entered
    private fun validateUserDetails():Boolean{
        return when{
            TextUtils.isEmpty(et_signup_email.text.toString().trim{it<=' '})->{
                showErrorSnackBar("Please Enter Email",true)
                false
            }

            TextUtils.isEmpty(et_signUp_first_name.text.toString().trim { it<=' '})->{
                showErrorSnackBar("Please Enter First Name",true)
                false
            }
            TextUtils.isEmpty(et_signUp_last_name.text.toString().trim { it<=' '})->{
                showErrorSnackBar("Please Enter Last Name",true)
                false
            }

            TextUtils.isEmpty(tv_signUp_dob.text.toString().trim{it<=' '})-> {
                showErrorSnackBar("Please Enter DOB",true)
                false
            }

            TextUtils.isEmpty(et_signUp_mobile.text.toString().trim { it<=' '})->{
                showErrorSnackBar("Please Enter Correct MobileNo",true)
                false
            }
            et_signUp_mobile.text.toString().trim { it<=' '}.length!=10->{
                showErrorSnackBar("Please Enter Correct MobileNo",true)
                false
            }
            et_signUp_password.text.toString().trim {it<=' '}!=et_signUp_confirm_password.text.toString().trim {it<=' '}->{
                showErrorSnackBar("Please Enter correct Password",true)
                false
            }
            else-> {
                true
            }
        }

    }


    //Image upload Success
    fun imageUploadSuccess(imageUrl:String){
        mUserProfileImageUri=imageUrl
        mProfileCompleted=1
        registerUser()
    }
    //user registered ---------------------------------------
    fun userRegisteredSuccessfully() {
        FirebaseAuth.getInstance().signOut()
        hideProgressDialog()
        showErrorSnackBar("User Registered Successfully..",false)
        finish()
    }

    override fun onClick(v: View?) {
        if (v!=null){
            when(v.id){
                R.id.iv_profile_register_image->{
                    //check for the permission for the storage
                    if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)
                        ==PackageManager.PERMISSION_GRANTED){
                            //TODO("choose image")
                            Constants.showImageChooser(this)
                        }
                    else{
                        //Request for the permission
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                            , Constants.READ_EXTERNAL_STORAGE_CODE)
                    }
                }

                R.id.ib_date_picker->{
                    CustomDatePicker(this).datePickerCustom(tv_signUp_dob)
                }
                R.id.btn_signUp->{
                    //Storing image than details
                    if (mSelectedImageFileUri!=null){
                        FireStoreClass().uploadImageToCloudStorage(this,mSelectedImageFileUri!!,
                            Constants.USER_PROFILE_IMAGE)
                    }
                    else{
                        registerUser()
                    }

                }
            }
        }
    }


    //Override function for the ona Activity result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode== Constants.READ_EXTERNAL_STORAGE_CODE){

            if (grantResults.isNotEmpty()&& grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Constants.showImageChooser(this)
            }
            else{
                Toast.makeText(this,"OOps you just denied",Toast.LENGTH_LONG).show()

            }
        }
    }


    //On Activity result-------------------------
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode== Activity.RESULT_OK){
            if (requestCode== Constants.PICK_IMAGE_REQUEST_CODE){
                if (data!=null){
                    try {
                        //Uri for the image
                        mSelectedImageFileUri=data.data!!

                        //load image using glide loader
                        GlideLoader(this).loadUserPicture(mSelectedImageFileUri!!,iv_profile_register_image)
                    }
                    catch (e:Exception){
                        e.printStackTrace()
                    }
                }
            }
        }
        else if (resultCode==Activity.RESULT_CANCELED){
            Log.e("Request cancelled","Image Selection cancelled")
        }
        else{
            throw RuntimeException()

            }
        }
}
