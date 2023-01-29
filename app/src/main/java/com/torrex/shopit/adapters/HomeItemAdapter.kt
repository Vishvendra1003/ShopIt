package com.torrex.shopit.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.torrex.shopit.R
import com.torrex.shopit.activities.ProductScreenActivity
import com.torrex.shopit.models.Product
import com.torrex.shopit.utils.Constants
import com.torrex.shopit.utils.GlideLoader
import kotlinx.android.synthetic.main.product_layout_custom.view.*

open class HomeItemAdapter(private val context:Context,private val list:ArrayList<Product>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.product_layout_custom,parent,false))
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model=list[position]

        if (holder is MyViewHolder){
            GlideLoader(context).loadUserPicture(model.productImage,holder.itemView.iv_home_product_image)
            holder.itemView.tv_home_product_name.text=model.productName
            holder.itemView.tv_home_product_price.text=model.productPrice.toString()
            holder.itemView.tv_home_product_des.text=model.productDescription

            holder.itemView.setOnClickListener{
                val intent=Intent(context, ProductScreenActivity::class.java)
                intent.putExtra(Constants.EXTRA_PRODUCT_ID,model.productId)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view:View):RecyclerView.ViewHolder(view)
}