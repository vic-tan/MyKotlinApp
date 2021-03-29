package com.common.utils

import android.content.Context
import com.blankj.utilcode.util.ConvertUtils
import com.common.widget.component.popup.BottomInputEditView
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.enums.PopupAnimation
import com.lxj.xpopup.interfaces.OnConfirmListener

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/7 10:03
 */
object ComDialogUtils {

    fun comConfirm(
        mContext: Context,
        content: String,
        confirmListener: OnConfirmListener
    ): BasePopupView {
        return comConfirm(mContext, "", content, confirmListener)
    }

    fun comConfirm(
        mContext: Context,
        title: String? = "",
        content: String,
        confirmListener: OnConfirmListener
    ): BasePopupView {
        return XPopup.Builder(mContext).popupAnimation(PopupAnimation.NoAnimation)
            .isDestroyOnDismiss(true)////对于只使用一次的弹窗,就释放资源
            .borderRadius(ConvertUtils.dp2px(10f).toFloat())
            .asConfirm(title, content, confirmListener).show()
    }

    /**
     * 输入信息框
     */
    fun showInputEditView(mContext: Context, callBack: BottomInputEditView.CallBack): BasePopupView {
        return XPopup.Builder(mContext)
            .autoOpenSoftInput(true)
            .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
            .asCustom(
                BottomInputEditView(
                    mContext,
                    callBack = callBack
                )
            )
            .show()
    }
}