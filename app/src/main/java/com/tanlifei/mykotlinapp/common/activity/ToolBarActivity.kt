package com.tanlifei.mykotlinapp.common.activity

import android.os.Bundle
import android.view.View

/**
 * 需要ActionBar的activity都必须继承本类
 */
open abstract class ToolBarActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setToolbar(View.VISIBLE)
    }

}