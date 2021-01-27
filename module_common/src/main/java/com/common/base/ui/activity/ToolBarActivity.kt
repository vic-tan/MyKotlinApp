package com.common.base.ui.activity

import android.os.Bundle
import android.view.View
import androidx.viewbinding.ViewBinding

/**
 * 需要ActionBar的activity都必须继承本类
 */
open abstract class ToolBarActivity<T : ViewBinding> : BaseActivity<T>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbar(View.VISIBLE)
    }

}