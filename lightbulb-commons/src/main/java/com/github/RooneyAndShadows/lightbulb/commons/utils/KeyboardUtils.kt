package com.github.RooneyAndShadows.lightbulb.commons.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment

class KeyboardUtils {
    companion object {
        @JvmStatic
        fun hideKeyboard(view: View?) {
            if (view == null) return
            val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.rootView.windowToken, 0)
        }

        @JvmStatic
        fun hideKeyboard(fragment: Fragment) {
            val ctx = fragment.context
            val view = fragment.view
            if (ctx == null || view == null) return
            val rootView = view.rootView
            val imm = ctx.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(rootView.windowToken, 0)
            view.clearFocus()
        }

        @JvmStatic
        fun hideKeyboard(activity: Activity) {
            val focusedView = activity.currentFocus
            focusedView?.clearFocus()
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val window = activity.window
            imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
        }

        @JvmStatic
        fun showKeyboard(forInput: EditText) {
            val imm = forInput.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(forInput, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}