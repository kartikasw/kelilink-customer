package com.kartikasw.kelilink.util

import java.text.NumberFormat
import java.util.*

fun String.withCurrencyFormat(): String =
    NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(this.toInt())

fun Int.withCurrencyFormat(): String =
    NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(this)