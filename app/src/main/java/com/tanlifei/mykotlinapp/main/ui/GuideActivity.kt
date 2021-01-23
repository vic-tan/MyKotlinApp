package com.tanlifei.mykotlinapp.main.ui

import com.tanlifei.mykotlinapp.R
import com.tanlifei.mykotlinapp.common.activity.BaseActivity
import com.tanlifei.mykotlinapp.main.adapter.GuideAdapter
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.activity_guide.*


/**
 * @desc: 引导界面
 * @author: tanlifei
 * @date: 2021/1/22 16:26
 */
class GuideActivity : BaseActivity() {


    var guideList: MutableList<Int> = ArrayList()
    override fun layoutResId(): Int {
        return R.layout.activity_guide
    }


    override fun showFullScreen(): Boolean {
        return true
    }


    override fun initView() {
        addGuideData()
        banner.adapter = GuideAdapter(this, guideList)
        banner.indicator = CircleIndicator(this)
        banner.isAutoLoop(false)
    }

    private fun addGuideData() {
        guideList.add(R.mipmap.icon_guide_one)
        guideList.add(R.mipmap.icon_guide_two)
        guideList.add(R.mipmap.icon_guide_three)
        guideList.add(R.mipmap.icon_guide_four)
    }

    /**
     * 不允许返回
     */
    override fun onBackPressed() {

    }
}