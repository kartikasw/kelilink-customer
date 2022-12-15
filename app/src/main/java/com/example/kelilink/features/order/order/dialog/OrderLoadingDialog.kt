package com.example.kelilink.features.order.order.dialog

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.example.kelilink.R

class OrderLoadingDialog(context: Context) {

    private var progress: AlertDialog

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(R.layout.dialog_loading_order, null)

        progress = AlertDialog.Builder(context)
            .setView(layout)
            .setCancelable(false)
            .create()
    }

    fun show() {
        progress.show()
    }

    fun dismiss() {
        progress.dismiss()
    }

}