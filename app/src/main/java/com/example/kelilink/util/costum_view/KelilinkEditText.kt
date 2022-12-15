package com.example.kelilink.util.costum_view

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import com.example.kelilink.R

class KelilinkEditText: AppCompatEditText {
    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(inputType == 129) {
                    if (s.toString().length < 6) {
                        error = resources.getText(R.string.error_field_password)
                    }
                }

                if(s.isEmpty()) {
                    error = resources.getText(R.string.error_field_empty)
                }
            }

            override fun afterTextChanged(s: Editable) {
                if(inputType == 33) {
                    if(!s.toString().isValidEmail()) {
                        error = resources.getText(R.string.error_field_email)
                    }
                }
            }
        })

    }

    private fun String.isValidEmail(): Boolean = this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}