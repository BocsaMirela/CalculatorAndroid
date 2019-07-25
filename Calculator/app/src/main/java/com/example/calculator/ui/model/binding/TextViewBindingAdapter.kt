package com.example.calculator.ui.model.binding

import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import org.jetbrains.anko.textColor

object TextViewBindingAdapter {

    @JvmStatic
    @BindingAdapter("android:color")
    fun setTextColor(view: TextView, @ColorRes color: Int?) {
        color?.let {
            view.textColor = ContextCompat.getColor(view.context, it)
        }
    }
}