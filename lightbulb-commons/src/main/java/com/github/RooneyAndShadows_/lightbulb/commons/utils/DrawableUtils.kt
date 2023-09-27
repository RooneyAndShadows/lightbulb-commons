package com.github.rooneyandshadows.lightbulb.commons.utils

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import androidx.core.graphics.ColorUtils
import com.github.rooneyandshadows.lightbulb.commons.views.WrappedDrawable

class DrawableUtils {
    companion object {

        @JvmStatic
        fun getRoundedBorderedShape(
            backgroundColor: Int,
            strokeColor: Int,
            left: Int,
            top: Int,
            right: Int,
            bottom: Int,
            topLeftRadius: Float,
            boottomLeftRadius: Float,
            topRightRadius: Float,
            bottomRightRadius: Float,
        ): LayerDrawable {
            return getBorders(
                backgroundColor,  // Background color
                strokeColor,  // Border color
                left,  // Left border in pixels
                top,  // Top border in pixels
                right,  // Right border in pixels
                bottom, // Bottom border in pixels
                topLeftRadius,
                boottomLeftRadius,
                topRightRadius,
                bottomRightRadius
            )
        }

        @JvmStatic
        fun getBorderedShape(
            backgroundColor: Int,
            strokeColor: Int,
            left: Int,
            top: Int,
            right: Int,
            bottom: Int,
        ): LayerDrawable {
            return getBorders(
                backgroundColor,  // Background color
                strokeColor,  // Border color
                left,  // Left border in pixels
                top,  // Top border in pixels
                right,  // Right border in pixels
                bottom // Bottom border in pixels
            )
        }

        @JvmStatic
        fun resizeDrawable(drawable: Drawable?, width: Int, height: Int): Drawable {
            val wrappedDrawable = WrappedDrawable(drawable)
            wrappedDrawable.setBounds(0, 0, width, height)
            return wrappedDrawable
        }

        @JvmStatic
        fun getLayeredRoundedCornersDrawable(
            backgroundColor: Int,
            foregroundColor: Int,
            width: Int?,
            height: Int?,
            cornerRadius: Int,
        ): Drawable {
            return getLayeredRoundedCornersDrawable(
                backgroundColor,
                foregroundColor,
                width,
                height,
                cornerRadius.toFloat(),
                cornerRadius.toFloat(),
                cornerRadius.toFloat(),
                cornerRadius.toFloat()
            )
        }

        @JvmStatic
        fun getRoundedCornersDrawable(
            color: Int,
            width: Int?,
            height: Int?,
            cornerRadius: Int,
        ): Drawable {
            return getRoundedCornersDrawable(
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
        fun getRoundedCornersDrawable(color: Int, cornerRadius: Int): Drawable {
            return getRoundedCornersDrawable(
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
        fun getLayeredRoundedCornersDrawable(
            backgroundColor: Int,
            foregroundColor: Int,
            cornerRadius: Int,
        ): Drawable {
            return getLayeredRoundedCornersDrawable(
                backgroundColor,
                foregroundColor,
                0,
                0,
                cornerRadius.toFloat(),
                cornerRadius.toFloat(),
                cornerRadius.toFloat(),
                cornerRadius.toFloat(),
            )
        }

        @JvmStatic
        fun getRoundedCornersDrawable(
            backgroundColor: Int,
            width: Int?,
            height: Int?,
            topLeftRadius: Float,
            topRightRadius: Float,
            bottomRightRadius: Float,
            bottomLeftRadius: Float,
        ): GradientDrawable {
            return getLayeredRoundedCornersDrawable(
                Color.TRANSPARENT,
                backgroundColor,
                width,
                height,
                topLeftRadius,
                topRightRadius,
                bottomRightRadius,
                bottomLeftRadius
            )
        }

        @JvmStatic
        fun getLayeredRoundedCornersDrawable(
            colorBackground: Int,
            colorForeground: Int,
            width: Int?,
            height: Int?,
            topLeftRadius: Float,
            topRightRadius: Float,
            bottomRightRadius: Float,
            bottomLeftRadius: Float,
        ): GradientDrawable {
            val gradientDrawable = GradientDrawable()
            gradientDrawable.cornerRadii = floatArrayOf(
                topLeftRadius, topLeftRadius,
                topRightRadius, topRightRadius,
                bottomRightRadius, bottomRightRadius,
                bottomLeftRadius, bottomLeftRadius
            )
            val color = ColorUtils.compositeColors(
                colorForeground,
                colorBackground
            )
            gradientDrawable.setColor(color)
            if (width != null && height != null && width > 0 && height > 0)
                gradientDrawable.setSize(width, height)
            return gradientDrawable
        }

        private fun getBorders(
            bgColor: Int,
            borderColor: Int,
            left: Int,
            top: Int,
            right: Int,
            bottom: Int,
            topLeftRadius: Float,
            boottomLeftRadius: Float,
            topRightRadius: Float,
            bottomRightRadius: Float,

            ): LayerDrawable {
            // Initialize new color drawables
            val borderColorDrawable = getRoundedCornersDrawable(
                borderColor,
                null,
                null,
                topLeftRadius,
                topRightRadius,
                bottomRightRadius,
                boottomLeftRadius
            )
            borderColorDrawable.setBounds(0, 0, 100, 2)
            val backgroundColorDrawable = getRoundedCornersDrawable(
                bgColor,
                null,
                null,
                topLeftRadius,
                topRightRadius,
                bottomRightRadius,
                boottomLeftRadius
            )
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

        private fun getBorders(
            bgColor: Int,
            borderColor: Int,
            left: Int,
            top: Int,
            right: Int,
            bottom: Int,
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