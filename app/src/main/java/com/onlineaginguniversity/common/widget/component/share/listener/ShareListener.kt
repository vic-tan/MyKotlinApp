package com.onlineaginguniversity.common.widget.component.share.listener

import android.view.View
import com.common.constant.GlobalEnumConst
import com.common.widget.component.extension.toast
import com.onlineaginguniversity.common.widget.component.share.bean.ShareBean

/**
 * @desc: 分享监听
 * @author: tanlifei
 * @date: 2021/2/25 15:01
 */
interface ShareListener {

    fun onClick(
        v: View,
        type: GlobalEnumConst.ShareType,
        shareBean: ShareBean?
    ) {
    }


    fun onComplete(
        type: GlobalEnumConst.ShareType,
    ) {
    }

    fun onError(
        type: GlobalEnumConst.ShareType,
        throws: Throwable?
    ) {
        toast("分享失败")
    }

    fun onCancel(
        type: GlobalEnumConst.ShareType,
    ) {
        toast("取消分享")
    }


}