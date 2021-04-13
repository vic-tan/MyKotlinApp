package com.common.core.share.listener

import android.view.View
import com.common.constant.GlobalEnumConst
import com.common.core.share.ui.ShareView

/**
 * @desc: 分享监听
 * @author: tanlifei
 * @date: 2021/2/25 15:01
 */
interface OnShareListener {
    fun onItemClick(
        v: View,
        type: GlobalEnumConst.ShareType
    ) {
    }
}