package com.torrex.shopit.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.View.OnClickListener
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.torrex.shopit.R
import com.torrex.shopit.firestore.FireStoreClass
import com.torrex.shopit.models.User
import com.torrex.shopit.utils.Constants
import com.torrex.shopit.utils.GlideLoader
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : BaseActivity(),OnClickListener {
    private lateinit var mUserDetails: User
    private var mSelectedProfileImage:Uri?=null
    private var mProfileImage:String=""
    private var mProfileComplete:Int=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)


        if(intent.hasExtra(Constants.PROFILE_USER_DETAILS)){
            //Get User Details from parcelable extra
            mUserDetails= intent.getParcelableExtra(Constants.PROFILE_USER_DETAILS)!!

            //Updating the details on UI------
            iv_user_profile_photo_edit.isEnabled=false
            et_profile_email.setText(mUserDetails.email)
            et_profile_email.isEnabled=false
            et_profile_first_name.setText(mUserDetails.firstName)
            et_profile_first_name.isEnabled=false
            et_profile_last_name.setText(mUserDetails.lastName)
            et_profile_last_name.isEnabled=false
            et_profile_mobile.setText((mUserDetails.mobile).toString())
            et_profile_mobile.isEnabled=false
            tv_user_profile_gender.text=mUserDetails.gender


            if (mUserDetails.image!=null){
                GlideLoader(this).loadUserPicture(mUserDetails.image,iv_user_profile_photo)
            }
        }



        //SetOnclickListener for the buttons
        btn_profile_edit.setOnClickListener(this)
        iv_user_profile_photo_edit.setOnClickListener(this)
        iv_log_out.setOnClickListener(this)
    }

    override fun onClick(view: View?) {

        when(view!!.id){

            R.id.btn_profile_edit->{
                if (btn_profile_edit.text==resources.getText(R.string.edit)){
                    showErrorSnackBar("Please Update your Profile",false)
                    sv_profile.setBackgroundColor(resources.getColor(R.color.light_blue))
                    iv_user_profile_photo_edit.isEnabled=true
                    et_profile_email.isEnabled=false
                    et_profile_first_name.isEnabled=true
                    et_profile_last_name.isEnabled=true
                    et_profile_mobile.isEnabled=true
                    btn_profile_edit.text=resources.getText(R.string.submit)
                }
                else if (btn_profile_edit.text==resources.getText(R.string.submit)){
                    showProgressDialog("Submitted!!")
                    if (mSelectedProfileImage!=null){
                        FireStoreClass().uploadImageToCloudStorage(this,mSelectedProfileImage!!
                            , Constants.USER_PROFILE_IMAGE)
                    }
                    else{
                        updateUserDetails()
                    }
                        Toast.makeText(this,"Submit Clicked",Toast.LENGTH_SHORT).show()
                    //TODO("show progress bar")
                    }


            }

            R.id.iv_user_profile_photo_edit->{
                if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)
                    ==PackageManager.PERMISSION_GRANTED){
                    Constants.showImageChooser(this)
                }
                else{
                    ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), Constants.READ_EXTERNAL_STORAGE_CODE)
                }
                Toast.makeText(this,"Image Clicked",Toast.LENGTH_SHORT).show()
            }

            R.id.iv_log_out->{
                FirebaseAuth.getInstance().signOut()
                val intent=Intent(this, LoginActivity::class.java)
                intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode==Activity.RESULT_OK){
            if (requestCode== Constants.PICK_IMAGE_REQUEST_CODE){
                if (data!=null){
                    mSelectedProfileImage=data.data!!
                    GlideLoader(this).loadUserPicture(mSelectedProfileImage!!,iv_user_profile_photo)
                }
            }
        }

    }



    //Updating Profile Success----------------------------------
    private fun updateUserDetails(){
        val userHashMap=HashMap<String,Any>()

        //Storing Keys and Values in the Hashmap
        val firstName=et_profile_first_name.text.toString().trim { it<=' ' }
        val lastName=et_profile_last_name.text.toString().trim { it<=' ' }
        val mobileNo=et_profile_mobile.text.toString().trim { it<=' ' }.toLong()

        if (firstName.isNotEmpty() && lastName.isNotEmpty()){
            userHashMap[Constants.FIRSTNAME]=firstName
            userHashMap[Constants.LASTNAME]=lastName
            userHashMap[Constants.MOBILE]=mobileNo
        }
        else{
            Toast.makeText(this,"Please enter all details",Toast.LENGTH_LONG).show()
        }

        if (mSelectedProfileImage!=null){
            userHashMap[Constants.IMAGE]=mProfileImage
            userHashMap[Constants.PROFILECOMPLETE]=mProfileComplete
        }

        FireStoreClass().updateUserDetails(this,userHashMap)

    }

    fun imageUploadSuccess(imageUrl:String){
        mProfileImage=imageUrl
        mProfileComplete=1
        updateUserDetails()
    }
    fun userProfileUpdateSuccess(){
        //TODO("Hide progress bar")
        hideProgressDialog()
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }
}