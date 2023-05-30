package com.github.rooneyandshadows.lightbulb.commons.utils

import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import java.util.*

@Suppress("unused")
class AppBarLayoutUtils {
    companion object {
        @JvmStatic
        fun setAppBarLayoutOffset(appBarLayout: AppBarLayout, offset: Int) {
            if (appBarLayout.layoutParams !is CoordinatorLayout.LayoutParams) return
            val param = appBarLayout.layoutParams as CoordinatorLayout.LayoutParams
            val behavior = param.behavior as AppBarLayout.Behavior?
            behavior?.let { it.topAndBottomOffset = offset }
        }

        @JvmStatic
        fun getAppBarLayoutOffset(appBarLayout: AppBarLayout): Int {
            if (appBarLayout.layoutParams !is CoordinatorLayout.LayoutParams) return 0
            val param = appBarLayout.layoutParams as CoordinatorLayout.LayoutParams
            val behavior = param.behavior as AppBarLayout.Behavior?
            return behavior?.topAndBottomOffset ?: 0
        }
    }
}