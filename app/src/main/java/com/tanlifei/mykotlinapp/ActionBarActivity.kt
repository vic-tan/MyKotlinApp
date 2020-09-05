package com.tanlifei.mykotlinapp

import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.ActivityUtils
import kotlinx.android.synthetic.main.base_actionbar.*


/**
 * 需要ActionBar的activity都必须继承本类
 */
open abstract class ActionBarActivity : BaseActivity() {

    protected lateinit var view: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = View.inflate(this, R.layout.base_actionbar, null)
        setContentView(view)
        initView()
        setToolbar(View.VISIBLE)
    }

    /**
     * 是否显示状态栏
     */

    open fun setToolbar(visibility: Int) {
        mActionBar.visibility = visibility
    }

    /**
     * 初始化View
     */
    fun initView() {
        mActionBar.setOnLeftTextClickListener {
            ActivityUtils.finishActivity(this)
        }
        val containerView: View = View.inflate(this, layoutResId(), null)
        container.addView(containerView)

    }

    abstract fun layoutResId(): Int


}