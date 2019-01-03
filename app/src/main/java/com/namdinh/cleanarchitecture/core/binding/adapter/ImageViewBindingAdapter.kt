package com.namdinh.cleanarchitecture.core.binding.adapter

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import com.namdinh.cleanarchitecture.core.glide.GlideApp

class ImageViewBindingAdapter {
    @BindingAdapter("imageUrl", "placeHolder")
    fun loadImage(view: ImageView, imageUrl: String?, placeholder: Drawable) {
        GlideApp.with(view.context).load(imageUrl).placeholder(placeholder).into(view)
    }

    @BindingAdapter("imageUrl", "placeHolder")
    fun loadImage(view: ImageView, imageUrl: String?, @DrawableRes placeholder: Int) {
        GlideApp.with(view.context).load(imageUrl).placeholder(placeholder).into(view)
    }
}
