package com.thomas.apps.testpaging.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.api.load

@BindingAdapter("imageUrl")
fun bindImage(imageView: ImageView, imageUrl: String?) {
    imageView.load(imageUrl)
}