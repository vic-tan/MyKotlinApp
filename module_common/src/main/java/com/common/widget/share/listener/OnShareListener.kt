package com.common.widget.share.listener

import android.view.View
import com.common.widget.share.ui.ShareView

/**
 * @desc: 分享监听
 * @author: tanlifei
 * @date: 2021/2/25 15:01
 */
interface OnShareListener {
    fun onItemClick(
        v: View,
        type: ShareView.ShareType
    ) {
    }
}