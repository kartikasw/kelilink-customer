package com.example.kelilink.util.costum_view

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.example.kelilink.R

class KelilinkLoadingDialog(context: Context) {

    private var progress: AlertDialog

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(R.layout.dialog_loading, null)

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