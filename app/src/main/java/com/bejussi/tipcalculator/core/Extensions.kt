package com.bejussi.tipcalculator.core

import android.content.Context
import android.widget.Toast

fun Context.makeToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}