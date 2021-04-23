package com.common.utils

import android.content.Context
import com.common.R
import com.common.widget.component.popup.BottomInputEditView
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.interfaces.OnCancelListener
import com.lxj.xpopup.interfaces.OnConfirmListener

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/7 10:03
 */
object ComDialogUtils {

    /**—————————————————————————————————————————————————— 双按钮的弹出框 ——————————————————————————————————————————————*/


    /**
     * 默认标题为“温馨提示”双按钮提示框
     */
    fun showMultDialog(
        mContext: Context,
        content: String,
        confirmListener: OnConfirmListener,
    ): BasePopupView {
        return baseMultDialog(
            mContext,
            title = "温馨提示",
            content = content,
            confirmListener = confirmListener
        )
    }

    /**
     * 自定义标题、内容按钮为确定双按钮提示框
     */
    fun showMultDialog(
        mContext: Context,
        title: String,
        content: String,
        confirmListener: OnConfirmListener,
    ): BasePopupView {
        return baseMultDialog(
            mContext,
            title = title,
            content = content,
            confirmListener = confirmListener
        )
    }

    /**
     * 自定义标题、内容、按钮文字双按钮提示框
     */
    fun showMultDialog(
        mContext: Context,
        title: String,
        content: String,
        confirmBtnText: String,
        confirmListener: OnConfirmListener,
    ): BasePopupView {
        return baseMultDialog(
            mContext,
            title = title,
            content = content,
            confirmBtnText = confirmBtnText,
            confirmListener = confirmListener
        )
    }


    /**
     * 没有标题且按钮默认的双按钮提示框
     */
    fun showMultDialogByNoTitle(
        mContext: Context?,
        content: String,
        confirmListener: OnConfirmListener,
    ): BasePopupView {
        return baseMultDialog(mContext, content = content, confirmListener = confirmListener)
    }

    /**
     * 没有标题自定义确定按钮文字的双按钮提示框
     */
    fun showMultDialogByNoTitle(
        mContext: Context,
        content: String,
        confirmBtnText: String = "确定",
        confirmListener: OnConfirmListener,
    ): BasePopupView {
        return baseMultDialog(
            mContext,
            content = content,
            confirmBtnText = confirmBtnText,
            confirmListener = confirmListener
        )
    }


    /**
     * 公用两个按钮提示框
     */
    private fun baseMultDialog(
        mContext: Context?,
        title: String? = null,
        content: String,
        cancelBtnText: String = "取消",
        confirmBtnText: String = "确定",
        confirmListener: OnConfirmListener? = null,
        cancelListener: OnCancelListener? = null,
        isDismissOnTouchOutside: Boolean = false
    ): BasePopupView {
        return XPopup.Builder(mContext)
            .dismissOnTouchOutside(isDismissOnTouchOutside)
            .asConfirm(
                title,
                content,
                cancelBtnText,
                confirmBtnText,
                confirmListener,
                cancelListener,
                false,
                R.layout.dialog_com_mult_layout
            ).show()
    }


    /**—————————————————————————————————————————————————— 单按钮弹出框  ——————————————————————————————————————————————*/

    /**
     * 默认标题为 “温馨提示” 单按钮提示框
     */
    fun showSingleDialog(
        mContext: Context,
        content: String,
        confirmListener: OnConfirmListener,
    ): BasePopupView {
        return baseMultDialog(
            mContext,
            title = "温馨提示",
            content = content,
            confirmListener = confirmListener
        )
    }


    /**
     * 自定义标题、内容单按钮提示框
     */
    fun showSingleDialog(
        mContext: Context,
        title: String,
        content: String,
        confirmListener: OnConfirmListener,
    ): BasePopupView {
        return baseSingleDialog(
            mContext,
            title = title,
            content = content,
            confirmListener = confirmListener
        )
    }


    /**
     * 自定义标题、内容、按钮文字单按钮提示框
     */
    fun showSingleDialog(
        mContext: Context,
        title: String,
        content: String,
        confirmBtnText: String,
        confirmListener: OnConfirmListener,
    ): BasePopupView {
        return baseSingleDialog(
            mContext,
            title = title,
            content = content,
            confirmBtnText = confirmBtnText,
            confirmListener = confirmListener
        )
    }

    /**
     * 没有标题且按钮默认的单按钮提示框
     */
    fun showSingleDialogByNoTitle(
        mContext: Context,
        content: String,
        confirmListener: OnConfirmListener,
    ): BasePopupView {
        return baseSingleDialog(mContext, content = content, confirmListener = confirmListener)
    }

    /**
     * 没有标题自定义确定按钮文字的单按钮提示框
     */
    fun showSingleDialogByNoTitle(
        mContext: Context,
        content: String,
        confirmBtnText: String = "确定",
        confirmListener: OnConfirmListener,
    ): BasePopupView {
        return baseSingleDialog(
            mContext,
            content = content,
            confirmBtnText = confirmBtnText,
            confirmListener = confirmListener
        )
    }


    /**—————————————————————————————————————————————————— 有标题的弹出框（单按钮）  ——————————————————————————————————————————————*/

    /**
     * 公用单个按钮提示框
     */
    private fun baseSingleDialog(
        mContext: Context,
        title: String? = null,
        content: String,
        confirmBtnText: String = "确定",
        confirmListener: OnConfirmListener? = null,
    ): BasePopupView {
        return XPopup.Builder(mContext)
            .dismissOnTouchOutside(false)
            .asConfirm(
                title,
                content,
                null,
                confirmBtnText,
                confirmListener,
                null,
                true,
                R.layout.dialog_com_single_layout
            ).show()
    }


    /**
     * 输入信息框
     */
    fun showInputEditView(
        mContext: Context,
        callBack: BottomInputEditView.CallBack
    ): BasePopupView {
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


    /**—————————————————————————————————————————————————— 标题的提示框  ——————————————————————————————————————————————*/
    /**
     * 只有一个按钮的提示框，红色背景按钮
     */
    fun showComPrompt(
        mContext: Context,
        title: String,
        content: String,
        confirmBtnText: String? = "我知道了"
    ): BasePopupView {
        return XPopup.Builder(mContext)
            .dismissOnTouchOutside(false)
            .asConfirm(
                title,
                content,
                "取消",
                confirmBtnText,
                null,
                null,
                true,
                R.layout.dialog_prompt
            ).show()
    }


}