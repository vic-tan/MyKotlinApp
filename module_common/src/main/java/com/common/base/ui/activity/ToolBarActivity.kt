package com.common.base.ui.activity

import android.os.Bundle
import android.view.View
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.ActivityUtils
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
                mActivity?.let { ActivityUtils.finishActivity(it) }
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
        immersionBar(){
            statusBarDarkFont(true)
        }
    }

}