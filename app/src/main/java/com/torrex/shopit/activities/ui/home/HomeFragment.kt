package com.torrex.shopit.activities.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.GridLayoutManager
import com.torrex.shopit.R
import com.torrex.shopit.databinding.FragmentHomeBinding
import com.torrex.shopit.activities.BaseFragment
import com.torrex.shopit.activities.CartActivity
import com.torrex.shopit.activities.ProfileActivity
import com.torrex.shopit.adapters.HomeItemAdapter
import com.torrex.shopit.firestore.FireStoreClass
import com.torrex.shopit.models.Product
import com.torrex.shopit.utils.Constants
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.user_profile_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId

        when(id){
            R.id.user_profile_details-> {
                val user = Constants.getCurrentProfileUser()
                val intent = Intent(activity, ProfileActivity::class.java)
                intent.putExtra(Constants.PROFILE_USER_DETAILS, user)
                startActivity(intent)
            }
            R.id.user_cart->{
                startActivity(Intent(activity,CartActivity::class.java))
            }
        }

        return super.onOptionsItemSelected(item)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }


    private fun getHomeItemList(){
        showProgressDialog("Loading....")
        FireStoreClass().getHomeProductList(this)
    }

    fun getHomeItemListSuccess(productList:ArrayList<Product>){
        hideProgressDialog()
        if (productList.size!=0){
            tv_no_product_online.visibility=View.GONE
            rv_home_product_view.visibility=View.VISIBLE

            rv_home_product_view.layoutManager=GridLayoutManager(activity,3)
            rv_home_product_view.setHasFixedSize(true)

            val adapter= HomeItemAdapter(requireActivity(),productList)
            rv_home_product_view.adapter=adapter
        }
        else{
            rv_home_product_view.visibility=View.GONE
            tv_no_product_online.visibility=View.VISIBLE
        }
    }

    //overridden methods----------------------------------
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        getHomeItemList()
    }
}