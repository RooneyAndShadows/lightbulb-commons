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

/*
Add to manifest
<provider
    android:name="androidx.core.content.FileProvider"
    android:authorities="net.budgetsight.android.fileprovider"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/file_paths" />
</provider>
======= file_paths.xml
<paths>
    <external-files-path
        name="images"
        path="Pictures" />
</paths>
 */
@Suppress("unused")
class CameraUtils {
    companion object {

        private const val FILE_NAME_KEY =
            "com.github.rooneyandshadows.lightbulb.commons.utils.FILE_NAME"

        @JvmStatic
        fun initializeCameraLauncher(
            fragment: Fragment,
            listeners: CameraListeners
        ): ActivityResultLauncher<Intent> {
            return fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val context = fragment.requireContext()
                    val directory = getExternalPicsDir(context)
                    val fileName = PreferenceUtils.getString(context, FILE_NAME_KEY, "").apply {
                        PreferenceUtils.clearKey(context, FILE_NAME_KEY)
                    }
                    if (fileName.isBlank()) {
                        listeners.onError(
                            result.resultCode,
                            "Failed to get filename for the saved image."
                        )
                        return@registerForActivityResult
                    }
                    val file = File(directory, fileName)
                    sendImageToPublicDir(context, file)
                    listeners.onPictureTaken(file)
                } else listeners.onError(result.resultCode, "")
            }
        }

        @JvmStatic
        fun initializeCameraLauncher(
            activity: AppCompatActivity,
            listeners: CameraListeners
        ): ActivityResultLauncher<Intent> {
            return activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val directory = getExternalPicsDir(activity)
                    val fileName = PreferenceUtils.getString(activity, FILE_NAME_KEY, "").apply {
                        PreferenceUtils.clearKey(activity, FILE_NAME_KEY)
                    }
                    if (fileName.isBlank()) {
                        listeners.onError(
                            result.resultCode,
                            "Failed to get filename for the saved image."
                        )
                        return@registerForActivityResult
                    }
                    val file = File(directory, fileName)
                    sendImageToPublicDir(activity, file)
                    listeners.onPictureTaken(file)
                } else listeners.onError(result.resultCode, "")
            }
        }

        @JvmStatic
        fun openCamera(
            launcher: ActivityResultLauncher<Intent>,
            fileProvider: String,
            context: Context,
            fileName: String,
        ) {
            val fname = fileName.plus(".jpg")
            val externalDirectory = getExternalPicsDir(context)
            val folder = File(externalDirectory, "")
            if (!folder.exists())
                folder.mkdirs()
            val file = File(folder, fname)
            val photoURI = FileProvider.getUriForFile(
                context,
                fileProvider,
                file
            )
            PreferenceUtils.saveString(context, FILE_NAME_KEY, fname)
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcher.launch(cameraIntent)
        }

        private fun contentValues(displayName: String): ContentValues {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.DISPLAY_NAME, displayName)
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
            return values
        }

        @Suppress("DEPRECATION")
        private fun sendImageToPublicDir(context: Context, sourceFile: File) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val values = contentValues(sourceFile.name)
                values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                values.put(MediaStore.Images.Media.IS_PENDING, true)
                val uri: Uri? = context.contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    values
                )
                if (uri != null) {
                    val outputStream = context.contentResolver.openOutputStream(uri)
                    FileSystemUtils.copy(sourceFile, outputStream!!)
                    values.put(MediaStore.Images.Media.IS_PENDING, false)
                    context.contentResolver.update(uri, values, null, null)
                }
            } else {
                val directory =
                    File(context.getExternalFilesDir(null), getApplicationName(context))
                if (!directory.exists()) {
                    println(directory.mkdirs())
                }
                val targetFile = File(directory, sourceFile.name)
                FileSystemUtils.copy(sourceFile, targetFile)
                val values = contentValues(sourceFile.name)
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
            fun onPictureTaken(imageFile: File)

            fun onError(resultCode: Int, details: String)
        }
    }
}