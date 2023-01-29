package com.torrex.shopit.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.torrex.shopit.R
import com.torrex.shopit.activities.MySellProduct
import com.torrex.shopit.activities.ProductSettingActivity
import com.torrex.shopit.models.Product
import com.torrex.shopit.utils.Constants
import com.torrex.shopit.utils.GlideLoader
import kotlinx.android.synthetic.main.item_list_layout.view.*

class ProductListAdapter(private val context: Context
,private val list: ArrayList<Product>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_list_layout,parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val productModel=list[position]

        if (holder is MyViewHolder){
            GlideLoader(context).loadUserPicture(productModel.productImage,holder.itemView.iv_product_image_list)
            holder.itemView.tv_product_name_list.text=productModel.productName
            holder.itemView.tv_product_price_list.text= productModel.productPrice.toString()
            holder.itemView.tv_product_des_list.text=productModel.productDescription

            holder.itemView.ib_product_delete_list.setOnClickListener{
                if (context is MySellProduct){
                    context.deleteProduct(context,productModel.productId)
                }
            }
        }
        holder.itemView.setOnClickListener{
            val intent=Intent(context, ProductSettingActivity::class.java)
            intent.putExtra(Constants.EXTRA_PRODUCT_ID,productModel.productId)
            intent.putExtra(Constants.EXTRA_PRODUCT_USER_ID,productModel.userId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view: View):RecyclerView.ViewHolder(view)
}