package com.thomas.apps.testpaging.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.api.load
import coil.size.Scale
import com.thomas.apps.testpaging.R

@BindingAdapter("imageUrl")
fun bindImage(imageView: ImageView, imageUrl: String?) {
    imageView.load(imageUrl){
        size(100,100)
        scale(Scale.FIT)
        error(R.drawable.ic_baseline_broken_image_24)
        placeholder(R.drawable.ic_baseline_image_24)
    }
}