package com.github.rooneyandshadows.lightbulb.commons.utils

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment


class PermissionUtils {
    companion object {

        @JvmStatic
        fun initializeRequestMultiplePermissionsLauncher(
            fragment: Fragment,
            listeners: PermissionListeners
        ): ActivityResultLauncher<Array<String>> {
            return fragment.registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { result ->
                var isGranted = true
                result.forEach { (_, v) ->
                    if (!v)
                        isGranted = false
                    return@forEach
                }
                if (isGranted) listeners.onGranted()
                else listeners.onRejected()
            }
        }

        @JvmStatic
        fun initializeRequestMultiplePermissionsLauncher(
            activity: AppCompatActivity,
            listeners: PermissionListeners
        ): ActivityResultLauncher<Array<String>> {
            return activity.registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { result ->
                var isGranted = true
                result.forEach { (_, v) ->
                    if (!v)
                        isGranted = false
                    return@forEach
                }
                if (isGranted) listeners.onGranted()
                else listeners.onRejected()
            }
        }

        @JvmStatic
        fun initializeRequestSinglePermissionLauncher(
            fragment: Fragment,
            listeners: PermissionListeners
        ): ActivityResultLauncher<String> {
            return fragment.registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted ->
                if (isGranted) listeners.onGranted()
                else listeners.onRejected()
            }
        }

        @JvmStatic
        fun initializeRequestSinglePermissionLauncher(
            activity: AppCompatActivity,
            listeners: PermissionListeners
        ): ActivityResultLauncher<String> {
            return activity.registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted ->
                if (isGranted) listeners.onGranted()
                else listeners.onRejected()
            }
        }

        interface PermissionListeners {
            fun onGranted()

            fun onRejected()
        }

        @JvmStatic
        fun launch(launcher: ActivityResultLauncher<String>, permission: String) {
            launcher.launch(permission)
        }

        @JvmStatic
        fun launch(launcher: ActivityResultLauncher<Array<String>>, permissions: Array<String>) {
            launcher.launch(permissions)
        }
    }
}