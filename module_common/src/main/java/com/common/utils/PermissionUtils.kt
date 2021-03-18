package com.common.utils

import android.Manifest
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.blankj.utilcode.util.ObjectUtils
import com.permissionx.guolindev.PermissionX
import java.util.*

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/22 11:08
 */
object PermissionUtils {

    fun requestPermission(
        context: Context,
        vararg permissions: String,
        callback: PermissionCallback
    ) {
        var permissionsList = ArrayList<String>()
        if (ObjectUtils.isNotEmpty(permissions)) {
            for (p in permissions) {
                permissionsList.add(p)
            }
        }
        if (context is FragmentActivity) {
            PermissionX.init(context)
                .permissions(permissionsList)
                .request { allGranted, _, deniedList ->
                    if (allGranted) {
                        callback.allGranted()
                    } else {
                        callback.denied(deniedList)
                    }
                }
        } else if (context is Fragment) {
            PermissionX.init(context)
                .permissions(permissionsList)
                .request { allGranted, _, deniedList ->
                    if (allGranted) {
                        callback.allGranted()
                    } else {
                        callback.denied(deniedList)
                    }
                }
        }

    }


    /**
     * 请求拍照权限
     */
    fun requestCameraPermission(
        context: Context,
        callback: PermissionCallback
    ) {
        requestPermission(
            context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            callback = callback
        )
    }

    interface PermissionCallback {
        fun allGranted()
        fun denied(deniedList: MutableList<String>) {}
    }


}