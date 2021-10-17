package com.github.RooneyAndShadows.lightbulb.commons.utils

import android.content.Context
import com.mikepenz.iconics.IconicsColor
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.iconics.utils.color
import com.mikepenz.iconics.utils.sizePx

class IconUtils {
    companion object {
        @JvmStatic
        fun getIconWithAttributeColor(context: Context, targetIcon: IIcon, targetColor: Int, size: Int): IconicsDrawable {
            return IconicsDrawable(context, targetIcon)
                .apply {
                    sizePx = size
                    color = IconicsColor.colorInt(
                        ResourceUtils.getColorByAttribute(
                            context,
                            targetColor
                        )
                    );
                }
        }

        @JvmStatic
        fun getIconWithResolvedColor(context: Context, targetIcon: IIcon, targetColor: Int, size: Int): IconicsDrawable {
            return IconicsDrawable(context, targetIcon)
                .apply {
                    sizePx = size
                    color = IconicsColor.colorInt(targetColor)
                }
        }
    }
}