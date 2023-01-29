package com.torrex.shopit.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.net.toUri
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.torrex.shopit.R
import com.torrex.shopit.utils.GlideLoader
import java.util.*
import kotlin.collections.ArrayList

class SlideImageAdapter(private val context:Context,private val list:ArrayList<String>):PagerAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view===`object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        //return super.instantiateItem(container, position)
        val inflater=LayoutInflater.from(context).inflate(R.layout.product_slidable_layout_small,container,false)!!
        val imageView=inflater.findViewById(R.id.iv_slide_product_image) as ImageView
        Glide.with(context).load(list[position]).into(imageView)

        container.addView(inflater,0)
        return inflater

    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        //super.destroyItem(container, position, `object`)
        container.removeView(`object`as RelativeLayout)
    }
}