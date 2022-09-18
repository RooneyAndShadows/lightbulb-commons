package com.github.rooneyandshadows.lightbulb.commons.utils

import androidx.viewpager2.widget.ViewPager2
import androidx.recyclerview.widget.RecyclerView

class ViewPager2Utils {
    companion object {
        fun reduceDragSensitivity(pager: ViewPager2?) {
            try {
                val ff = ViewPager2::class.java.getDeclaredField("mRecyclerView")
                ff.isAccessible = true
                val recyclerView = ff[pager] as RecyclerView
                val touchSlopField = RecyclerView::class.java.getDeclaredField("mTouchSlop")
                touchSlopField.isAccessible = true
                val touchSlop = touchSlopField[recyclerView] as Int
                touchSlopField[recyclerView] = touchSlop * 3
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
        }
    }
}