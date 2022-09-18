package com.github.rooneyandshadows.lightbulb.commons.utils

import android.content.Context
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class FileSystemUtils {
    companion object {
        const val sizeKb: Long = 1000
        const val sizeMb = sizeKb * sizeKb
        const val sizeGb = sizeMb * sizeKb
        const val sizeTerra = sizeGb * sizeKb
        const val kbUnit = "KB"
        const val mbUnit = "MB"
        const val gbUnit = "GB"
        private const val unitUpTreshhold = 15
        fun getKbToBytes(kb: Long): Long {
            return kb * sizeKb
        }

        fun getMbToBytes(mb: Long): Long {
            return mb * sizeMb
        }

        fun getGbToBytes(gb: Long): Long {
            return gb * sizeGb
        }

        fun getFileSizeWithUnit(size: Long): String {
            return (size / getFileSizeUnitSize(size)).toString() + getFileSizeUnitName(size)
        }

        private fun getFileSizeUnitSize(size: Long): Long {
            if (size < sizeMb * unitUpTreshhold) return sizeKb
            if (size < sizeGb * unitUpTreshhold) return sizeMb
            return if (size < sizeTerra * unitUpTreshhold) sizeGb else sizeMb
        }

        private fun getFileSizeUnitName(size: Long): String {
            if (size < sizeMb * unitUpTreshhold) return kbUnit
            if (size < sizeGb * unitUpTreshhold) return mbUnit
            return if (size < sizeTerra * unitUpTreshhold) gbUnit else mbUnit
        }

        fun createFileInLocalStorage(
            context: Context,
            destinationDir: String,
            sFileName: String,
            inputStream: InputStream
        ): File {
            val dir = File(context.filesDir, destinationDir)
            if (!dir.exists())
                dir.mkdir()
            val file = File(dir, sFileName)
            FileOutputStream(file).use { output ->
                BufferedInputStream(inputStream).use { input ->
                    val dataBuffer = ByteArray(4096)
                    var readBytes: Int
                    var totalBytes: Long = 0
                    while (input.read(dataBuffer).also { readBytes = it } != -1) {
                        totalBytes += readBytes.toLong()
                        output.write(dataBuffer, 0, readBytes)
                    }
                }
            }
            return file
        }

        fun getFileFromLocalStorage(context: Context, path: String): File {
            return File(context.filesDir, path)
        }
    }
}