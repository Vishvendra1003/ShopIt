package com.torrex.shopit.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.torrex.shopit.R
import com.torrex.shopit.models.CartItem
import com.torrex.shopit.utils.GlideLoader
import com.torrex.shopit.activities.MyAddressActivity
import com.torrex.shopit.utils.Constants
import kotlinx.android.synthetic.main.order_list_view.view.*

class CartItemListAdapter(private val context: Context,private val list:ArrayList<CartItem>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener:OnClickListener?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.order_list_view,parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val model=list[position]

        if (holder is MyViewHolder){
            GlideLoader(context).loadUserPicture(model.image,holder.itemView.iv_order_image_list)
            holder.itemView.tv_order_product_name_list.text=model.title
            holder.itemView.tv_order_product_price_list.text= model.price.toString()
            holder.itemView.tv_order_id_list.text=model.id

        }

        holder.itemView.setOnClickListener{
            if (onClickListener!=null){
                onClickListener!!.onClick(position,model)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view: View):RecyclerView.ViewHolder(view)

    fun setOnClickListener(onClickListener:OnClickListener){
        this.onClickListener=onClickListener
    }
    interface OnClickListener{
        fun onClick(position:Int,cartItem:CartItem)
    }

}