package com.app.simplesearch.utils
import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.app.simplesearch.R
import com.google.android.material.snackbar.Snackbar

fun onSNACK(view: View, ctx: Context, msg:String){
    //Snackbar(view)
    val snackbar = Snackbar.make(view, msg,
        Snackbar.LENGTH_SHORT).setAction("Action", null)
    snackbar.setActionTextColor(
        ContextCompat.getColor(ctx, R.color.colorWhite))
    val snackbarView = snackbar.view
    snackbarView.setBackgroundColor(ContextCompat.getColor(ctx, R.color.colorRedShade1))
    val textView =
        snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
    textView.setTextColor(ContextCompat.getColor(ctx, R.color.colorWhite))
    val typeface = ResourcesCompat.getFont(ctx, R.font.poppins_regular)
    textView.textSize = 12f
    textView.typeface = typeface
    snackbar.show()
}


