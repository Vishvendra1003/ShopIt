package com.torrex.shopit.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

class CustomButton(context: Context, attrs: AttributeSet?) : AppCompatButton(context, attrs) {

    init {
        applyButton()
    }
    private fun applyButton(){
        val typeface=Typeface.createFromAsset(context.assets,"Montserrat-Bold.ttf")
        setTypeface(typeface)
    }
}