package com.github.rooneyandshadows.lightbulb.commons.utils

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment

class ExternalFileUtils {
    companion object {
        @JvmStatic
        fun downloadFile(
            fileName: String,
            notificationDescription: String,
            urlString: String,
            headers: MutableMap<String, String> = mutableMapOf(),
            context: Context
        ) {
            val request = DownloadManager.Request(Uri.parse(urlString)).apply {
                setTitle(fileName)
                setDescription(notificationDescription)
                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                setDestinationInExternalFilesDir(
                    context,
                    Environment.DIRECTORY_DOWNLOADS,
                    fileName
                )
                headers.forEach { (name, value) ->
                    addRequestHeader(name, value)
                }
            }
            (context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager).enqueue(request)
        }
    }
}