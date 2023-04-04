package com.github.rooneyandshadows.lightbulb.commons.utils

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.SparseArray
import com.github.rooneyandshadows.java.commons.date.DateUtils
import com.github.rooneyandshadows.java.commons.date.DateUtilsOffsetDate
import java.io.Serializable
import java.time.OffsetDateTime
import java.util.*

@Suppress("unused")
class BundleUtils {
    companion object {
        @JvmStatic
        fun putOffsetDateTime(key: String, dest: Bundle, date: OffsetDateTime?): Companion {
            if (date != null) dest.putString(
                key,
                DateUtilsOffsetDate.getDateStringInDefaultFormat(date)
            )
            return Companion
        }

        @JvmStatic
        fun putDate(key: String, dest: Bundle, date: Date?): Companion {
            if (date != null) dest.putString(key, DateUtils.getDateStringInDefaultFormat(date))
            return Companion
        }

        @JvmStatic
        fun putInt(key: String, dest: Bundle, integer: Int): Companion {
            dest.putInt(key, integer)
            return Companion
        }

        @JvmStatic
        fun putString(key: String, dest: Bundle, string: String?): Companion {
            dest.putString(key, string)
            return Companion
        }

        @JvmStatic
        fun putDouble(key: String, dest: Bundle, value: Double): Companion {
            dest.putDouble(key, value)
            return Companion
        }

        @JvmStatic
        fun putFloat(key: String, dest: Bundle, value: Float): Companion {
            dest.putFloat(key, value)
            return Companion
        }

        @JvmStatic
        fun putBoolean(key: String, dest: Bundle, bool: Boolean): Companion {
            dest.putBoolean(key, bool)
            return Companion
        }

        @JvmStatic
        fun putUUID(key: String, dest: Bundle, uuid: UUID?): Companion {
            if (uuid != null) dest.putString(key, uuid.toString())
            return Companion
        }

        @JvmStatic
        fun putParcelable(key: String, dest: Bundle, parcelable: Parcelable?): Companion {
            dest.putParcelable(key, parcelable)
            return Companion
        }

        @JvmStatic
        fun putStringList(key: String, dest: Bundle, list: List<String>?): Companion {
            dest.putStringArrayList(key, list as ArrayList<String>)
            return Companion
        }

        @JvmStatic
        fun <T : Parcelable> putSparseParcelableArray(
            key: String,
            dest: Bundle,
            sparseParcelableArray: SparseArray<T>?,
        ): Companion {
            dest.putSparseParcelableArray(key, sparseParcelableArray)
            return Companion
        }

        @JvmStatic
        fun <T : Parcelable> putParcelableList(
            key: String,
            dest: Bundle,
            list: List<T>?,
        ): Companion {
            dest.putParcelableArrayList(key, list as ArrayList<T>)
            return Companion
        }

        @JvmStatic
        fun putSerializable(
            key: String,
            dest: Bundle,
            serializable: Serializable?,
        ): Companion {
            dest.putSerializable(key, serializable)
            return Companion
        }

        @JvmStatic
        fun getOffsetDateTime(key: String, source: Bundle): OffsetDateTime? {
            val dateString = source.getString(key)
            if (dateString.isNullOrBlank()) return null
            return DateUtilsOffsetDate.getDateFromStringInDefaultFormat(dateString)
        }

        @JvmStatic
        fun getDate(key: String, source: Bundle): Date? {
            val dateString = source.getString(key)
            if (dateString.isNullOrBlank()) return null
            return DateUtils.getDateFromStringInDefaultFormat(dateString)
        }

        @JvmStatic
        fun getInt(key: String, source: Bundle): Int {
            return source.getInt(key)
        }

        @JvmStatic
        fun getString(key: String, source: Bundle): String? {
            return source.getString(key)
        }

        @JvmStatic
        fun getDouble(key: String, source: Bundle): Double {
            return source.getDouble(key)
        }

        @JvmStatic
        fun getFloat(key: String, source: Bundle): Float {
            return source.getFloat(key)
        }

        @JvmStatic
        fun getBoolean(key: String, source: Bundle): Boolean {
            return source.getBoolean(key)
        }

        @JvmStatic
        fun getUUID(key: String, source: Bundle): UUID? {
            val uuidString = source.getString(key)
            if (uuidString.isNullOrBlank()) return null
            return UUID.fromString(key)
        }

        @Suppress("DEPRECATION")
        @JvmStatic
        fun <T : Parcelable> getParcelable(
            key: String,
            source: Bundle,
            clazz: Class<T>,
        ): T? {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) source.getParcelable(key, clazz)
            else source.getParcelable(key)
        }

        @JvmStatic
        fun getStringList(key: String, source: Bundle): List<String>? {
            return source.getStringArrayList(key)
        }

        @Suppress("DEPRECATION")
        @JvmStatic
        fun <V : Parcelable> getSparseParcelableArray(
            key: String,
            source: Bundle,
            clazz: Class<V>,
        ): SparseArray<V>? {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) source.getSparseParcelableArray(key, clazz)
            else source.getSparseParcelableArray(key)
        }

        @Suppress("DEPRECATION")
        @JvmStatic
        fun <V : Parcelable> getParcelableList(
            key: String,
            source: Bundle,
            clazz: Class<V>,
        ): List<V>? {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) source.getParcelableArrayList(key, clazz)
            else source.getParcelableArrayList(key)
        }

        @Suppress("DEPRECATION", "UNCHECKED_CAST")
        @JvmStatic
        fun <V : Serializable> getSerializable(
            key: String,
            source: Bundle,
            clazz: Class<V>,
        ): V? {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) source.getSerializable(key, clazz)
            else source.getSerializable(key) as V
        }
    }
}