package com.mx.btapplication

import android.content.Context
import androidx.appcompat.app.AlertDialog

fun Context.showAlertDialog(message: String, action: () -> Unit,
                            btnNeg: String? = null, actionNeg:  (() -> Unit?)? = null) {

    val builder = AlertDialog.Builder(this)
    builder.setTitle(getString(R.string.app_name))
        .setMessage(message)
        .setPositiveButton(getString(R.string.lbl_accept)) { dialog, _ ->
            dialog.dismiss()
            action()
        }

    if (!btnNeg.isNullOrEmpty() && actionNeg != null) {
        builder.setNegativeButton(btnNeg) { dialog, _ ->
            dialog.dismiss()
            actionNeg()
        }
    }

    builder.show()

}