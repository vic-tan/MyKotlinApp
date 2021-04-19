package com.common.widget.component.popup

import android.content.Context
import com.common.R
import com.lxj.xpopup.core.CenterPopupView


/**
 * @desc:请求接口是加载中提示框
 * @author: tanlifei
 * @date: 2021/2/1 17:40
 */
class CustomLoadingView(mContext: Context) :
    CenterPopupView(mContext) {

    // 返回自定义弹窗的布局
    override fun getImplLayoutId(): Int {
        return R.layout.layout_dailog_loading
    }
}