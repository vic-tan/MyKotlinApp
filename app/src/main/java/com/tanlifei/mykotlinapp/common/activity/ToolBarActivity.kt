package com.tanlifei.mykotlinapp.common.activity

import android.view.View

/**
 * 需要ActionBar的activity都必须继承本类
 */
open abstract class ToolBarActivity : BaseActivity() {


    override fun layoutResId(): Int {
        return -1
    }

    override fun initView() {
        setToolbar(View.VISIBLE)
    }



}