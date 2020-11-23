package com.thetechannel.android.planit.util

import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter

@BindingAdapter("app:spinner_items")
fun setSpinnerItems(spinner: Spinner, sourceList: List<String>?) {
    if (sourceList == null) return
    val adapter = ArrayAdapter<String>(spinner.context, android.R.layout.simple_spinner_item, sourceList)
    spinner.adapter = adapter
}