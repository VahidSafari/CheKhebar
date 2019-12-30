package com.example.chekhebar.core

import androidx.databinding.BindingAdapter
import com.google.android.material.textview.MaterialTextView

@BindingAdapter("extractPersianString")
fun extractPersianString(view: MaterialTextView, text: String) {
    val persianTextStartIndex = text.indexOf('|') + 1
    view.text = text.substring(persianTextStartIndex)
}

@BindingAdapter("setDistance")
fun setDistance(view: MaterialTextView, distance: Int) {
    view.text = "فاصله: $distance متر"
}