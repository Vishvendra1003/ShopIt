package com.torrex.shopit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.torrex.shopit.activities.DashboardActivity
import com.torrex.shopit.activities.LoginActivity
import com.torrex.shopit.activities.ProfileActivity
import com.torrex.shopit.firestore.FireStoreClass
import com.torrex.shopit.models.User
import com.torrex.shopit.utils.Constants

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        @Suppress("DEPRICATION")
        Handler().postDelayed(
            {
                if (FireStoreClass().getCurrentUserID().isNotEmpty()){
                    FireStoreClass().getUserDetails(this)
                    Toast.makeText(this,"Wait Logging in with the user Id",Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this,"Current user is not there in data base",Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                } },2000)

    }

    fun userDetailsSuccess(user: User){
        if (user.profileCompleted==1){
            val intent=Intent(this, DashboardActivity::class.java)
            intent.putExtra(Constants.PROFILE_USER_DETAILS,user)
            startActivity(intent)
            finish()
        }
        else{
            val intent=Intent(this, ProfileActivity::class.java)
            intent.putExtra(Constants.PROFILE_USER_DETAILS,user)
            startActivity(intent)
            finish()
        }
    }
}