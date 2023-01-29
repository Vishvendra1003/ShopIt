package com.torrex.shopit.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.torrex.shopit.R
import com.torrex.shopit.models.Address
import kotlinx.android.synthetic.main.address_list_layout.view.*

open class AddressListAdapter(private val context: Context,private val list:ArrayList<Address>):RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var onclickListener: OnClickListener?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.address_list_layout,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyViewHolder){
            val model=list[position]

            holder.itemView.et_my_address.text=("${model.addressLine1} ${model.addressLine2} " +
                    "${model.addressLine3} : ${model.addressCity} , ${model.addressState}")
            holder.itemView.et_my_address_mobile.text=model.addressMobileNO.toString()
            holder.itemView.et_my_address_postal_code.text=model.addressPostalCode.toString()


            //on click view listener--------
            holder.itemView.setOnClickListener{
                if (onclickListener!=null){
                    onclickListener!!.onClick(position,model)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setOnClickListener(onclickListener: OnClickListener){
        this.onclickListener=onclickListener
    }

    interface OnClickListener{
        fun onClick(position: Int,address: Address)
    }
    class MyViewHolder(view:View):RecyclerView.ViewHolder(view)

}