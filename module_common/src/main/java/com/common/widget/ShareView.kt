package com.common.widget

import android.content.Context
import android.widget.TextView
import com.common.R
import com.lxj.xpopup.core.BottomPopupView

/**
 * @desc:分享
 * @author: tanlifei
 * @date: 2021/2/24 14:43
 */
class ShareView(context: Context) : BottomPopupView(context) {
    override fun getImplLayoutId(): Int {
        return R.layout.layout_share
    }

    override fun onCreate() {
        super.onCreate()
        findViewById<TextView>(R.id.cancel).setOnClickListener {
            dismiss()
        }
    }
}