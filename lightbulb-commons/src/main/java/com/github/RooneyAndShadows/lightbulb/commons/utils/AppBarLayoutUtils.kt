package com.github.rooneyandshadows.lightbulb.commons.utils

import android.R.integer.config_shortAnimTime
import android.animation.ValueAnimator
import android.view.animation.DecelerateInterpolator
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import java.lang.Integer.max
import java.util.*

@Suppress("unused")
class AppBarLayoutUtils {
    companion object {
        @JvmStatic
        @JvmOverloads
        fun setAppBarLayoutOffset(
            appBarLayout: AppBarLayout,
            offset: Int,
            animate: Boolean = true,
            duration: Int = ResourceUtils.getIntById(appBarLayout.context, config_shortAnimTime),
        ) {
            if (appBarLayout.layoutParams !is CoordinatorLayout.LayoutParams) return
            if (animate) animateOffset(appBarLayout, offset, max(0, duration))
            else setAppBarLayoutOffsetInternally(appBarLayout, offset)
        }

        @JvmStatic
        fun getAppBarLayoutOffset(appBarLayout: AppBarLayout): Int {
            if (appBarLayout.layoutParams !is CoordinatorLayout.LayoutParams) return 0
            val param = appBarLayout.layoutParams as CoordinatorLayout.LayoutParams
            val behavior = param.behavior as AppBarLayout.Behavior?
            return behavior?.topAndBottomOffset ?: 0
        }

        @JvmStatic
        private fun setAppBarLayoutOffsetInternally(appBarLayout: AppBarLayout, offset: Int) {
            if (appBarLayout.layoutParams !is CoordinatorLayout.LayoutParams) return
            val param = appBarLayout.layoutParams as CoordinatorLayout.LayoutParams
            val behavior = param.behavior as AppBarLayout.Behavior?
            behavior?.let { it.topAndBottomOffset = offset }
        }

        @JvmStatic
        private fun animateOffset(appBarLayout: AppBarLayout, newOffset: Int, duration: Int) {
            val valueAnimator = ValueAnimator.ofInt()
            valueAnimator.interpolator = DecelerateInterpolator()
            valueAnimator.addUpdateListener { animation ->
                val newValue = (animation.animatedValue as Int)
                setAppBarLayoutOffsetInternally(appBarLayout, newValue)
                appBarLayout.requestLayout()
            }
            valueAnimator.setIntValues(getAppBarLayoutOffset(appBarLayout), newOffset)
            valueAnimator.duration = duration.toLong()
            valueAnimator.start()
        }
    }
}