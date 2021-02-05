package com.tanlifei.app.home.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.common.base.navigator.NavigatorView
import com.common.utils.MyLogTools
import com.ruffian.library.widget.RTextView
import com.tanlifei.app.R
import com.tanlifei.app.databinding.NavigatorHomeTabBinding

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/1/23 17:23
 */
class HomeNavigatorView : NavigatorView<NavigatorHomeTabBinding> {


    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    )

    override fun navigatorLayoutResId(): Int {
        return R.layout.navigator_home_tab
    }

    override fun createBinding(layoutView: View): NavigatorHomeTabBinding {
        return NavigatorHomeTabBinding.bind(layoutView)
    }

    override fun normalImageArray(): IntArray? {
        return intArrayOf(
            R.mipmap.ic_tab_one_normal,
            R.mipmap.ic_tab_live_normal,
            R.mipmap.ic_tab_video_normal,
            R.mipmap.ic_tab_msg_normal,
            R.mipmap.ic_tab_personal_normal
        )
    }

    override fun pressImageArray(): IntArray? {
        return intArrayOf(
            R.mipmap.ic_tab_one_press,
            R.mipmap.ic_tab_live_press,
            R.mipmap.ic_tab_video_press,
            R.mipmap.ic_tab_msg_press,
            R.mipmap.ic_tab_personal_press
        )
    }

    /**
     * 显示学习数
     */
    open fun getMsgBadge(): RTextView {
        return binding.msgBadge
    }




}