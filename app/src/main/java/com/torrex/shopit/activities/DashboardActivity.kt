package com.torrex.shopit.activities

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.torrex.shopit.R
import com.torrex.shopit.databinding.ActivityDashboardBinding
import com.torrex.shopit.firestore.FireStoreClass
import com.torrex.shopit.models.User
import com.torrex.shopit.utils.Constants

class DashboardActivity : BaseActivity() {

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView


        val navController = findNavController(R.id.nav_host_fragment_activity_dashboard)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_account, R.id.navigation_payment,R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        FireStoreClass().getUserDetails(this)
    }
    fun getUserProfileSuccess(user: User){
        Constants.setCurrentProfileUser(user)
    }

    override fun onBackPressed() {
        doubleBackToExit()
    }
}