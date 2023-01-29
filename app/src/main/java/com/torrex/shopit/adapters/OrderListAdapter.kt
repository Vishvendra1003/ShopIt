package com.torrex.shopit.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.torrex.shopit.R
import com.torrex.shopit.activities.OrderPlacedActivity
import com.torrex.shopit.models.Order
import com.torrex.shopit.utils.Constants
import com.torrex.shopit.utils.GlideLoader
import kotlinx.android.synthetic.main.order_list_view.view.*

open class OrderListAdapter(private val context: Context,private val list:ArrayList<Order>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.order_list_view,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var orderId:String=""
        if (holder is MyViewHolder){
            val model=list[position]
            orderId=model.id

            if (model.image!=null){
                GlideLoader(context).loadUserPicture(model.image,holder.itemView.iv_order_image_list)
            }
            holder.itemView.tv_order_id_list.text=model.id
            holder.itemView.tv_order_product_name_list.text=model.title
            holder.itemView.tv_order_product_des_list.text=model.product.productDescription
            holder.itemView.tv_order_product_price_list.text=model.totalAmount.toString()
        }

        holder.itemView.iv_order_details.setOnClickListener{
            val intent= Intent(context, OrderPlacedActivity::class.java)
            intent.putExtra(Constants.ORDER_PLACED_INVOICE,orderId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view: View):RecyclerView.ViewHolder(view)
}