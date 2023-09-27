package com.github.rooneyandshadows.lightbulb.commons.utils

import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.core.content.ContextCompat

class ResourceUtils {
    companion object {
        @JvmStatic
        fun readNullableColorAttributeFromTypedArray(
            ctx: Context?,
            attributesTypedArray: TypedArray,
            target: Int
        ): Int? {
            val colorId = attributesTypedArray.getResourceId(target, -1)
            return if (colorId == -1) null else getColorById(ctx, colorId)
        }

        @JvmStatic
        fun getColorByAttribute(ctx: Context, attributeColorId: Int?): Int {
            val value = TypedValue()
            ctx.theme.resolveAttribute(attributeColorId!!, value, true)
            return value.data
        }

        @JvmStatic
        fun getColorById(ctx: Context?, colorId: Int?): Int {
            return ContextCompat.getColor(ctx!!, colorId!!)
        }

        @JvmStatic
        fun getIntById(ctx: Context, id: Int): Int {
            return ctx.resources.getInteger(id)
        }

        @JvmStatic
        fun getPhrase(ctx: Context, phraseId: Int?): String {
            return ctx.getString(phraseId!!)
        }

        @JvmStatic
        fun getDimenById(ctx: Context, id: Int): Float {
            return ctx.resources.getDimension(id)
        }

        @JvmStatic
        fun getDimenPxById(ctx: Context, id: Int): Int {
            return ctx.resources.getDimensionPixelSize(id)
        }

        @JvmStatic
        fun dpToPx(dp: Int): Int {
            return (dp * Resources.getSystem().displayMetrics.density).toInt()
        }

        @JvmStatic
        fun spToPx(sp: Int): Int {
            return (sp * Resources.getSystem().displayMetrics.scaledDensity).toInt()
        }

        @JvmStatic
        fun getDrawable(ctx: Context?, drawableId: Int?): Drawable? {
            return getDrawable(ctx, drawableId, false)
        }

        @JvmStatic
        fun getDrawable(ctx: Context?, drawableId: Int?, mutate: Boolean = false): Drawable? {
            return if (mutate) ContextCompat.getDrawable(ctx!!, drawableId!!)?.mutate()
            else ContextCompat.getDrawable(ctx!!, drawableId!!)
        }

        @JvmStatic
        fun getStatusBarHeight(ctx: Context): Int {
            var result = 0
            val resourceId = ctx.resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = ctx.resources.getDimensionPixelSize(resourceId)
            }
            return result
        }
    }
}