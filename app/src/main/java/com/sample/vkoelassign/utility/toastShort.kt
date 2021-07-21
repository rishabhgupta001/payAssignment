package com.sample.vkoelassign.utility

import android.content.Context
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast



/**
 *
 * Purpose – Common Extension Functions
 *
 * @author Rishabh Gupta
 *
 * */
fun Context.toastShort(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun ProgressBar.show() {
    visibility = View.VISIBLE
}

fun ProgressBar.hide() {
    visibility = View.GONE
}

