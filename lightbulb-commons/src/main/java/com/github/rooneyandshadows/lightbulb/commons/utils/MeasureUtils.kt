package com.github.rooneyandshadows.lightbulb.commons.utils

import android.view.View
import android.view.View.MeasureSpec.*

class MeasureUtils {
    companion object {

        @JvmStatic
        fun measureViewUnspecified(view: View) {
            view.measure(
                makeMeasureSpec(0, UNSPECIFIED),
                makeMeasureSpec(0, UNSPECIFIED)
            )
        }

        /**
         * Measure dimension according to measure spec and desired size
         *
         * ------- CUSTOM VIEW -------
         * ...........................
         * override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
         *     val desiredWidth = suggestedMinimumWidth + paddingLeft + paddingRight
         *     val desiredHeight = suggestedMinimumHeight + paddingTop + paddingBottom
         *     setMeasuredDimension(
         *         measureDimension(desiredWidth, widthMeasureSpec),
         *         measureDimension(desiredHeight, heightMeasureSpec)
         *     )
         * }
         * ...........................
         *------- CUSTOM VIEW -------
         *
         * @param desiredSize
         * @param measureSpec
         * @return
         *
         */
        @JvmStatic
        fun measureDimension(desiredSize: Int, measureSpec: Int): Int {
            var result: Int
            val specMode = getMode(measureSpec)
            val specSize = getSize(measureSpec)
            if (specMode == EXACTLY) {
                result = specSize
            } else {
                result = desiredSize
                if (specMode == AT_MOST) {
                    result = Math.min(result, specSize)
                }
            }
            return result
        }
    }
}