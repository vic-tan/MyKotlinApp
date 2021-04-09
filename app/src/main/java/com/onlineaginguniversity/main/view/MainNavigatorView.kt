package com.onlineaginguniversity.main.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.common.core.navigator.NavigatorView
import com.ruffian.library.widget.RTextView
import com.onlineaginguniversity.R
import com.onlineaginguniversity.databinding.NavigatorMainTabBinding

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/1/23 17:23
 */
class MainNavigatorView @JvmOverloads constructor(
    mContext: Context,
    mAttributeSet: AttributeSet? = null,
    mDefStyleAttr: Int = 0
) : NavigatorView<NavigatorMainTabBinding>(mContext, mAttributeSet, mDefStyleAttr) {


    override fun navigatorLayoutResId(): Int {
        return R.layout.navigator_main_tab
    }

    override fun createBinding(layoutView: View): NavigatorMainTabBinding {
        return NavigatorMainTabBinding.bind(layoutView)
    }

    override fun normalImageArray(): IntArray? {
        return intArrayOf(
            R.mipmap.ic_tab_home,
            R.mipmap.ic_tab_class,
            R.mipmap.ic_tab_classmate_circle,
            R.mipmap.ic_tab_study,
            R.mipmap.ic_tab_profile
        )
    }

    override fun pressImageArray(): IntArray? {
        return intArrayOf(
            R.mipmap.ic_tab_home_pre,
            R.mipmap.ic_tab_class_pre,
            R.mipmap.ic_tab_classmate_circle_pre,
            R.mipmap.ic_tab_study_pre,
            R.mipmap.ic_tab_profile_pre
        )
    }

    /**
     * 显示学习数
     */
    open fun getMsgBadge(): RTextView {
        return mBinding.msgBadge
    }


}