package com.rands.lightbulb.commons.utils

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import com.rands.lightbulb.commons.views.WrappedDrawable

class DrawableUtils {
    companion object {
        @JvmStatic
        fun resizeDrawable(drawable: Drawable?, width: Int, height: Int): Drawable {
            val wrappedDrawable = WrappedDrawable(drawable)
            wrappedDrawable.setBounds(0, 0, width, height)
            return wrappedDrawable
        }

        @JvmStatic
        fun getRoundedShapeWithColor(color: Int, width: Int, height: Int, cornerRadius: Int): Drawable {
            return generateRoundedCornersDrawable(
                color,
                width,
                height,
                cornerRadius.toFloat(),
                cornerRadius.toFloat(),
                cornerRadius.toFloat(),
                cornerRadius.toFloat()
            )
        }

        @JvmStatic
        fun getRoundedShapeWithColor(
            color: Int,
            topLeftRadius: Int,
            topRightRadius: Int,
            bottomRightRadius: Int,
            bottomLeftRadius: Int,
            size: Int,
        ): Drawable {
            return generateRoundedCornersDrawable(
                color,
                size,
                size,
                topLeftRadius.toFloat(),
                topRightRadius.toFloat(),
                bottomRightRadius.toFloat(),
                bottomLeftRadius.toFloat()
            )
        }

        @JvmStatic
        fun getRoundedShapeWithColor(
            color: Int,
            topLeftRadius: Int,
            topRightRadius: Int,
            bottomRightRadius: Int,
            bottomLeftRadius: Int
        ): Drawable {
            return generateRoundedCornersDrawable(
                color,
                0,
                0,
                topLeftRadius.toFloat(),
                topRightRadius.toFloat(),
                bottomRightRadius.toFloat(),
                bottomLeftRadius.toFloat()
            )
        }

        @JvmStatic
        fun getRoundedShapeWithColor(color: Int, size: Int, cornerRadius: Int): Drawable {
            return generateRoundedCornersDrawable(
                color,
                size,
                size,
                cornerRadius.toFloat(),
                cornerRadius.toFloat(),
                cornerRadius.toFloat(),
                cornerRadius.toFloat(),
            )
        }

        @JvmStatic
        fun getRoundedShapeWithColor(color: Int, cornerRadius: Int): Drawable {
            return generateRoundedCornersDrawable(
                color,
                0,
                0,
                cornerRadius.toFloat(),
                cornerRadius.toFloat(),
                cornerRadius.toFloat(),
                cornerRadius.toFloat(),
            )
        }

        @JvmStatic
        fun getBorderedShape(backgroundColor: Int, strokeColor: Int, left: Int, top: Int, right: Int, bottom: Int): LayerDrawable {
            return getBorders(
                backgroundColor,  // Background color
                strokeColor,  // Border color
                left,  // Left border in pixels
                top,  // Top border in pixels
                right,  // Right border in pixels
                bottom // Bottom border in pixels
            )
        }

        private fun generateRoundedCornersDrawable(
            backgroundColor: Int,
            width: Int?,
            height: Int?,
            topLeftRadius: Float,
            topRightRadius: Float,
            bottomRightRadius: Float,
            bottomLeftRadius: Float
        ): GradientDrawable {
            val gradientDrawable = GradientDrawable()
            gradientDrawable.cornerRadii = floatArrayOf(
                topLeftRadius, topLeftRadius,
                topRightRadius, topRightRadius,
                bottomRightRadius, bottomRightRadius,
                bottomLeftRadius, bottomLeftRadius
            )
            gradientDrawable.setColor(backgroundColor)
            if (width != null && height != null && width > 0 && height > 0)
                gradientDrawable.setSize(width, height)
            return gradientDrawable
        }

        private fun getBorders(
            bgColor: Int, borderColor: Int,
            left: Int, top: Int, right: Int, bottom: Int
        ): LayerDrawable {
            // Initialize new color drawables
            val borderColorDrawable = ColorDrawable(borderColor)
            borderColorDrawable.setBounds(0, 0, 100, 2)
            val backgroundColorDrawable = ColorDrawable(bgColor)
            // Initialize a new array of drawable objects
            val drawables = arrayOf<Drawable>(
                borderColorDrawable,
                backgroundColorDrawable
            )

            // Initialize a new layer drawable instance from drawables array
            val layerDrawable = LayerDrawable(drawables)


            // Set padding for background color layer
            layerDrawable.setLayerInset(
                1,  // Index of the drawable to adjust [background color layer]
                left,  // Number of pixels to add to the left bound [left border]
                top,  // Number of pixels to add to the top bound [top border]
                right,  // Number of pixels to add to the right bound [right border]
                bottom // Number of pixels to add to the bottom bound [bottom border]
            )

            // Finally, return the one or more sided bordered background drawable
            return layerDrawable
        }
    }
}