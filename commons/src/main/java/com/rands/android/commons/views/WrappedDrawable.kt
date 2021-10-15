package com.rands.lightbulb.commons.views

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable

class WrappedDrawable(protected val drawable: Drawable?) : Drawable() {
    override fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
        //update bounds to get correctly
        super.setBounds(left, top, right, bottom)
        val drawable = drawable
        drawable?.setBounds(left, top, right, bottom)
    }

    override fun setAlpha(alpha: Int) {
        val drawable = drawable
        if (drawable != null) {
            drawable.alpha = alpha
        }
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        val drawable = drawable
        if (drawable != null) {
            drawable.colorFilter = colorFilter
        }
    }

    @Suppress("DEPRECATION")
    override fun getOpacity(): Int {
        val drawable = drawable
        return drawable?.opacity ?: PixelFormat.UNKNOWN
    }

    override fun draw(canvas: Canvas) {
        val drawable = drawable
        drawable?.draw(canvas)
    }

    override fun getIntrinsicWidth(): Int {
        val drawable = drawable
        return drawable?.bounds?.width() ?: 0
    }

    override fun getIntrinsicHeight(): Int {
        val drawable = drawable
        return drawable?.bounds?.height() ?: 0
    }
}