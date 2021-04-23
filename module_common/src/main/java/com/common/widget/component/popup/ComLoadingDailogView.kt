package com.common.widget.component.popup

import android.content.Context
import com.common.R
import com.common.databinding.LayoutDailogLoadingBinding
import com.common.databinding.LayoutImageViewerPopupBinding
import com.common.databinding.LayoutOptionBinding
import com.lxj.xpopup.core.CenterPopupView


/**
 * @desc:请求接口是加载中提示框
 * @author: tanlifei
 * @date: 2021/2/1 17:40
 */
class ComLoadingDailogView(mContext: Context) :
    CenterPopupView(mContext) {

    lateinit var mBinding: LayoutDailogLoadingBinding
    // 返回自定义弹窗的布局
    override fun getImplLayoutId(): Int {
        return R.layout.layout_dailog_loading
    }

    override fun onCreate() {
        super.onCreate()
        mBinding = LayoutDailogLoadingBinding.bind(popupImplView)
    }

    /**
     * 设置标题
     */
    fun setTitle(title:String){
        post {
            mBinding.tvTitle.text = title
        }

    }
}