package com.common.base.ui.activity

import android.os.Bundle
import android.view.View
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.ActivityUtils
import com.common.R
import com.common.base.viewmodel.BaseViewModel
import com.common.widget.component.extension.visible
import com.gyf.immersionbar.ktx.immersionBar
import com.hjq.bar.OnTitleBarListener


/**
 * 带头部标题栏基类
 */
open abstract class BaseToolBarActivity<V : ViewBinding, VM : BaseViewModel> :
    BaseActivity<V, VM>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbar(visibleToolbar())
        setTitleBarListener()
    }


    open fun setTitleBarListener() {
        mTitleBar.setOnTitleBarListener(object : OnTitleBarListener {
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
        mBaseBinding.statusBarView.visible()
        immersionBar() {
            statusBarDarkFont(true, 0.2f)
            statusBarView(mBaseBinding.statusBarView)
            statusBarColor(R.color.white)
        }
    }

    open fun visibleToolbar(): Boolean {
        return true
    }


}