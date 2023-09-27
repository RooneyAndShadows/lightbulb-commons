package com.github.rooneyandshadows.lightbulb.commons.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar

class InteractionUtils {
    companion object {
        @JvmStatic
        fun showMessage(context: Context, errorText: String?) {
            ContextCompat.getMainExecutor(context).execute {
                try {
                    Toast.makeText(context, errorText, Toast.LENGTH_LONG).show()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        @JvmStatic
        fun showSnackBar(
            rootView: View,
            errorText: String,
            actionTitle: String?,
            actionListener: View.OnClickListener?,
        ) {
            ContextCompat.getMainExecutor(rootView.context).execute {
                try {
                    val bar = Snackbar.make(rootView, errorText, Snackbar.LENGTH_LONG)
                    if (actionTitle != null && actionTitle != "") bar.setAction(
                        actionTitle,
                        actionListener
                    )
                    bar.show()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}