package com.tanlifei.mykotlinapp.home.view

import android.content.Context
import com.tanlifei.mykotlinapp.R
import com.tanlifei.mykotlinapp.core.navigator.BaseNavigatorView

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/1/23 17:23
 */
class NavigatorView : BaseNavigatorView {


    constructor(context: Context) : super(context) {
    }

    override fun navigatorLayoutResId(): Int {
        return R.layout.navigator_home_tab
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


}