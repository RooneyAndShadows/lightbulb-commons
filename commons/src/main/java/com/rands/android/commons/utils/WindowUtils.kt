package com.rands.lightbulb.commons.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowInsets
import android.view.WindowMetrics
import androidx.core.content.ContextCompat
import java.lang.Exception

class WindowUtils {
    companion object {

        @Suppress("DEPRECATION")
        @JvmStatic
        fun getWindowHeight(activity: Activity): Int {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val windowMetrics = activity.windowManager.currentWindowMetrics
                val insets = windowMetrics.windowInsets
                    .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
                windowMetrics.bounds.height() - insets.top - insets.bottom
            } else {
                val displayMetrics = DisplayMetrics()
                activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
                var statusBarHeight = 0
                try {
                    val statusBarHeightId: Int = activity.resources.getIdentifier("status_bar_height", "dimen", "android")
                    statusBarHeight = activity.resources.getDimensionPixelSize(statusBarHeightId)
                } catch (ignore: Exception) {
                }
                displayMetrics.heightPixels - statusBarHeight
            }
        }

        @Suppress("DEPRECATION")
        @JvmStatic
        fun getWindowWidth(activity: Activity): Int {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val windowMetrics = activity.windowManager.currentWindowMetrics
                val insets = windowMetrics.windowInsets
                    .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
                windowMetrics.bounds.width() - insets.left - insets.right
            } else {
                val displayMetrics = DisplayMetrics()
                activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
                displayMetrics.widthPixels
            }
        }
    }
}