package com.example.calculator.ui.model.binding

import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter

object ViewBindingAdapter {

    @JvmStatic
    @BindingAdapter("android:visible")
    fun setVisible(view: View, visible: Boolean) {
        view.visibility = if (visible) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("android:invisible")
    fun setInvisible(view: View, invisible: Boolean) {
        view.visibility = if (invisible) View.INVISIBLE else View.VISIBLE
    }

    @JvmStatic
    @BindingAdapter("android:background")
    fun setBackground(view: View, @ColorRes color: Int?) {
        if (color == null) {
            view.background = null
        } else {
            view.setBackgroundResource(color)
        }
    }


    @JvmStatic
    @BindingAdapter("android:backgroundTint")
    fun setBackgroundTint(view: View, @ColorRes color: Int?) {
        if (color == null) {
            view.backgroundTintList = null
        } else {
            view.backgroundTintList = ContextCompat.getColorStateList(view.context, color)
            view.background = view.background
        }
    }
}
