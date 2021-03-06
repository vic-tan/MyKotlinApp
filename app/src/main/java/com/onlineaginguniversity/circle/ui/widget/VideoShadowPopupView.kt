package com.onlineaginguniversity.circle.ui.widget

import android.content.Context
import android.view.View.OnClickListener
import com.common.widget.component.extension.clickListener
import com.lxj.xpopup.impl.PartShadowPopupView
import com.onlineaginguniversity.R
import com.onlineaginguniversity.circle.bean.CircleBean
import com.onlineaginguniversity.databinding.LayoutVideoShadowPopupBinding

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/3/30 17:51
 */
class VideoShadowPopupView(val mContext: Context, var item: CircleBean) :
    PartShadowPopupView(mContext) {
    lateinit var mBinding: LayoutVideoShadowPopupBinding
    override fun getImplLayoutId(): Int {
        return R.layout.layout_video_shadow_popup
    }

    override fun onCreate() {
        super.onCreate()
        mBinding = LayoutVideoShadowPopupBinding.bind(popupImplView)
        mBinding.apply {
            content.text = item.content
        }
        clickListener(
            mBinding.shortcut,
            clickListener = OnClickListener {
                when (it) {
                    mBinding.shortcut -> {
                        dismiss()
                    }
                }
            }
        )

    }
}