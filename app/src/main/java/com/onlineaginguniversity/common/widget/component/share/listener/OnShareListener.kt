package com.onlineaginguniversity.common.widget.component.share.listener

import android.view.View
import com.common.constant.GlobalEnumConst

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