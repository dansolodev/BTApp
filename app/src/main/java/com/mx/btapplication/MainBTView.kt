package com.mx.btapplication

import android.app.Activity

interface MainBTView {
    fun getCurrentActivity(): Activity
    fun showErrorBTNotSupport()
    fun showError(message: String, function: () -> Unit)
    fun requestPermissionBTEnable()
    fun showLoading()
    fun initAdapter(elements: MutableList<DeviceBT>?)
}