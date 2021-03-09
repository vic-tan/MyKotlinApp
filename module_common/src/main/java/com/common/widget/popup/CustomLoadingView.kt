package com.common.widget.popup

import android.content.Context
import com.common.R
import com.lxj.xpopup.core.CenterPopupView


/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/1 17:40
 */
class CustomLoadingView(context: Context) :
    CenterPopupView(context) {

    // 返回自定义弹窗的布局
    override fun getImplLayoutId(): Int {
        return R.layout.layout_dailog_loading
    }
}