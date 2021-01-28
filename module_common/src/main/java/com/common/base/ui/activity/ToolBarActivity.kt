package com.common.base.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ConvertUtils
import com.common.R
import com.common.utils.LogTools
import com.gyf.immersionbar.ktx.actionBarHeight
import com.gyf.immersionbar.ktx.immersionBar
import com.hjq.bar.OnTitleBarListener


/**
 * 需要ActionBar的activity都必须继承本类
 */
open abstract class ToolBarActivity<T : ViewBinding> : BaseActivity<T>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbar(View.VISIBLE)
        titleBar.setOnTitleBarListener(object : OnTitleBarListener {
            override fun onLeftClick(v: View) {
                ActivityUtils.finishActivity(mActivity)
            }

            override fun onTitleClick(v: View) {
            }

            override fun onRightClick(v: View) {
            }
        })
    }

    /**
     * 沉浸式
     */
    override fun initImmersionBar() {
        baseBinding.statusBarView.visibility = View.VISIBLE
        immersionBar() {
            statusBarDarkFont(true, 0.2f)
            statusBarView(baseBinding.statusBarView)
            statusBarColor(R.color.white)
        }
    }


}