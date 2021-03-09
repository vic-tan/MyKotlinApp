package com.common.utils

import android.content.Context
import com.blankj.utilcode.util.ConvertUtils
import com.common.widget.popup.BottomInputEditView
import com.hjq.toast.ToastUtils
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
        context: Context,
        content: String,
        confirmListener: OnConfirmListener
    ): BasePopupView {
        return comConfirm(context, "", content, confirmListener)
    }

    fun comConfirm(
        context: Context,
        title: String? = "",
        content: String,
        confirmListener: OnConfirmListener
    ): BasePopupView {
        return XPopup.Builder(context).popupAnimation(PopupAnimation.NoAnimation)
            .isDestroyOnDismiss(true)////对于只使用一次的弹窗,就释放资源
            .borderRadius(ConvertUtils.dp2px(10f).toFloat())
            .asConfirm(title, content, confirmListener).show()
    }

    /**
     * 输入信息框
     */
    fun showInputEditView(context: Context, callBack: BottomInputEditView.CallBack): BasePopupView {
        return XPopup.Builder(context)
            .autoOpenSoftInput(true)
            .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
            .asCustom(
                BottomInputEditView(
                    context,
                    callBack = callBack
                )
            )
            .show()
    }
}