package com.common.core.share.listener

import com.common.constant.GlobalEnumConst
import java.util.*

/**
 * @desc: 分享监听
 * @author: tanlifei
 * @date: 2021/2/25 15:01
 */
interface OnAuthListener {

    fun onComplete(
        type: GlobalEnumConst.ShareType,
        prams: HashMap<String, Any>?
    )

    fun onError(
        type: GlobalEnumConst.ShareType,
        throws: Throwable?
    ) {
    }

    fun onCancel(
        type: GlobalEnumConst.ShareType,
    ) {
    }
}