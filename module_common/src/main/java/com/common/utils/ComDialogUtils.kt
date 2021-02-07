package com.common.utils

import android.content.Context
import com.blankj.utilcode.util.ConvertUtils
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.interfaces.OnConfirmListener

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/7 10:03
 */
object ComDialogUtils {

    fun comConfirm(
        context: Context,
        content: String,
        confirmListener: OnConfirmListener
    ): BasePopupView {
        return comConfirm(context, content = content, confirmListener = confirmListener)
    }

    fun comConfirm(
        context: Context,
        title: String? = "",
        content: String,
        confirmListener: OnConfirmListener
    ): BasePopupView {
        return XPopup.Builder(context).borderRadius(ConvertUtils.dp2px(10f).toFloat())
            .asConfirm(title, content, confirmListener).show()
    }
}