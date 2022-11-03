package com.github.rooneyandshadows.lightbulb.commons.utils

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.github.rooneyandshadows.lightbulb.commons.utils.FileSystemUtils.Companion.getExternalPicsDir
import java.io.File


class CameraUtils {
    companion object {

        @JvmStatic
        fun initializeCameraLauncher(
            fragment: Fragment,
            listeners: CameraListeners
        ): ActivityResultLauncher<Intent> {
            return fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    sendImageToPublicDir(fragment.requireContext())
                    listeners.onPictureTaken(result.data!!)
                } else listeners.onError(result.resultCode)
            }
        }

        @JvmStatic
        fun initializeCameraLauncher(
            activity: AppCompatActivity,
            listeners: CameraListeners
        ): ActivityResultLauncher<Intent> {
            return activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    listeners.onPictureTaken(result.data!!)
                } else listeners.onError(result.resultCode)
            }
        }

        @JvmStatic
        fun openCamera(
            fileName: String,
            context: Context,
            fileProvider: String,
            launcher: ActivityResultLauncher<Intent>
        ) {
            val externalDirectory = getExternalPicsDir(context)
            val folder = File(externalDirectory, "MY_APP_PICS")
            if (!folder.exists())
                folder.mkdirs()
            val file = File(folder, fileName.plus(".jpg"))
            val photoURI = FileProvider.getUriForFile(
                context,
                fileProvider,
                file
            )
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcher.launch(cameraIntent)
        }

        private fun contentValues(): ContentValues {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
            return values
        }

        private fun sendImageToPublicDir(context: Context) {
            val externalDirectory = getExternalPicsDir(context)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val values = contentValues()
                values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                values.put(MediaStore.Images.Media.IS_PENDING, true)
                val uri: Uri? = context.contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    values
                )
                if (uri != null) {
                    val outputStream = context.contentResolver.openOutputStream(uri)
                    val folder = File(externalDirectory, "MY_APP_PICS")
                    val file = File(folder, "MyPhoto.jpg")
                    FileSystemUtils.copy(file, outputStream!!)
                    values.put(MediaStore.Images.Media.IS_PENDING, false)
                    context.contentResolver.update(uri, values, null, null)
                }
            } else {
                val directory =
                    File(context.getExternalFilesDir(null), getApplicationName(context))
                if (!directory.exists()) {
                    println(directory.mkdirs())
                }
                val sourceFileDirectory = File(externalDirectory, "MY_APP_PICS")
                val sourceFile = File(sourceFileDirectory, "MyPhoto.jpg")
                val fileName = System.currentTimeMillis().toString() + ".jpg"
                val targetFile = File(directory, fileName)
                FileSystemUtils.copy(sourceFile, targetFile)
                val values = contentValues()
                values.put(MediaStore.Images.Media.DATA, targetFile.absolutePath)
                context.contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    values
                )
            }
        }


        @JvmStatic
        private fun getApplicationName(context: Context): String {
            val applicationInfo = context.applicationInfo
            val stringId = applicationInfo.labelRes
            return if (stringId == 0) applicationInfo.nonLocalizedLabel.toString() else context.getString(
                stringId
            )
        }

        interface CameraListeners {
            fun onPictureTaken(data: Intent)

            fun onError(resultCode: Int)
        }
    }
}