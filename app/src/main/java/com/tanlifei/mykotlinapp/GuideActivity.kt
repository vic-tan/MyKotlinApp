package com.tanlifei.mykotlinapp

import com.tanlifei.mykotlinapp.common.activity.BaseActivity

/**
 * @desc: 引导界面
 * @author: tanlifei
 * @date: 2021/1/22 16:26
 */
class GuideActivity : BaseActivity() {


    override fun layoutResId(): Int {
        return R.layout.activity_guide
    }


    override fun showFullScreen(): Boolean {
        return true
    }

    override fun initView() {

    }

    /**
     * 不允许返回
     */
    override fun onBackPressed() {

    }
}