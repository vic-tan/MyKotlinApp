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
        mContext: Context,
        vararg permissions: String,
        callback: PermissionCallback
    ) {
        var permissionsList = ArrayList<String>()
        if (ObjectUtils.isNotEmpty(permissions)) {
            for (p in permissions) {
                permissionsList.add(p)
            }
        }
        if (mContext is FragmentActivity) {
            PermissionX.init(mContext)
                .permissions(permissionsList)
                .request { allGranted, _, deniedList ->
                    if (allGranted) {
                        callback.allGranted()
                    } else {
                        callback.denied(deniedList)
                    }
                }
        } else if (mContext is Fragment) {
            PermissionX.init(mContext)
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
        mContext: Context,
        callback: PermissionCallback
    ) {
        requestPermission(
            mContext,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            callback = callback
        )
    }

    /**
     * 请求SDCard权限
     */
    fun requestSDCardPermission(
        mContext: Context,
        callback: PermissionCallback
    ) {
        requestPermission(
            mContext,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            callback = callback
        )
    }

    interface PermissionCallback {
        fun allGranted()
        fun denied(deniedList: MutableList<String>) {}
    }


}