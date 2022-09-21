package com.github.rooneyandshadows.lightbulb.commons.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.AssetFileDescriptor
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class FileSystemUtils {
    companion object {
        private const val sizeKb: Long = 1000
        private const val sizeMb = sizeKb * sizeKb
        private const val sizeGb = sizeMb * sizeKb
        private const val sizeTerra = sizeGb * sizeKb
        private const val kbUnit = "KB"
        private const val mbUnit = "MB"
        private const val gbUnit = "GB"
        private const val unitUpTreshhold = 15

        @JvmStatic
        fun getKbToBytes(kb: Long): Long {
            return kb * sizeKb
        }

        @JvmStatic
        fun getMbToBytes(mb: Long): Long {
            return mb * sizeMb
        }

        @JvmStatic
        fun getGbToBytes(gb: Long): Long {
            return gb * sizeGb
        }

        @JvmStatic
        fun getFileSizeWithUnit(size: Long): String {
            return (size / getFileSizeUnitSize(size)).toString() + getFileSizeUnitName(size)
        }

        @JvmStatic
        private fun getFileSizeUnitSize(size: Long): Long {
            if (size < sizeMb * unitUpTreshhold) return sizeKb
            if (size < sizeGb * unitUpTreshhold) return sizeMb
            return if (size < sizeTerra * unitUpTreshhold) sizeGb else sizeMb
        }

        @JvmStatic
        private fun getFileSizeUnitName(size: Long): String {
            if (size < sizeMb * unitUpTreshhold) return kbUnit
            if (size < sizeGb * unitUpTreshhold) return mbUnit
            return if (size < sizeTerra * unitUpTreshhold) gbUnit else mbUnit
        }

        @JvmStatic
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

        @JvmStatic
        fun openFilePicker(
            fragment: Fragment,
            filePickerCallbacks: FilePickerCallbacks
        ) {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val uri: Uri = result.data?.data!!
                    val fileName = getFileName(fragment.requireContext(), uri)
                    val fileSize = getFileSize(fragment.requireContext(), uri)
                    val inputStream = getFileInputStream(fragment.requireContext(), uri)
                    filePickerCallbacks.onFileChosen(fileName, fileSize, inputStream)
                }
            }.launch(intent)
        }

        @JvmStatic
        fun openFilePicker(
            activity: AppCompatActivity,
            filePickerCallbacks: FilePickerCallbacks
        ) {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val uri: Uri = result.data?.data!!
                    val fileName = getFileName(activity, uri)
                    val fileSize = getFileSize(activity, uri)
                    val inputStream = getFileInputStream(activity, uri)
                    filePickerCallbacks.onFileChosen(fileName, fileSize, inputStream)
                }
            }.launch(intent)
        }

        @JvmStatic
        fun getFileFromLocalStorage(context: Context, path: String): File {
            return File(context.filesDir, path)
        }

        private fun getFileInputStream(context: Context, uri: Uri): InputStream {
            return context.contentResolver.openInputStream(uri)!!
        }

        private fun getFileSize(context: Context, uri: Uri): Long {
            val fileDescriptor: AssetFileDescriptor =
                context.contentResolver.openAssetFileDescriptor(uri, "r")!!
            return fileDescriptor.length
        }

        private fun getFileName(context: Context, uri: Uri): String {
            val cursor = context.contentResolver?.query(uri, null, null, null, null)
            var filename: String? = null
            cursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)?.let { nameIndex ->
                cursor.moveToFirst()
                filename = cursor.getString(nameIndex)
                cursor.close()
            }
            return filename!!
        }

        interface FilePickerCallbacks {
            fun onFileChosen(fileName: String, fileSize: Long, inputStream: InputStream)
        }
    }
}