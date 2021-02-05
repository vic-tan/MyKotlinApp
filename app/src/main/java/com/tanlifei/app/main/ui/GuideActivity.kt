package com.tanlifei.app.main.ui

import com.blankj.utilcode.util.ActivityUtils
import com.common.core.base.ui.activity.BaseActivity
import com.tanlifei.app.R
import com.tanlifei.app.databinding.ActivityGuideBinding
import com.tanlifei.app.main.adapter.GuideAdapter
import com.youth.banner.indicator.CircleIndicator


/**
 * @desc: 引导界面
 * @author: tanlifei
 * @date: 2021/1/22 16:26
 */
class GuideActivity : BaseActivity<ActivityGuideBinding>() {

    var guideList: MutableList<Int> = ArrayList()

    companion object {
        fun actionStart() {
            ActivityUtils.startActivity(GuideActivity::class.java)
        }
    }




    override fun showFullScreen(): Boolean {
        return true
    }


    override fun init() {
        addGuideData()
        binding.banner.adapter = GuideAdapter(this, guideList)
        binding.banner.indicator = CircleIndicator(this)
        binding.banner.isAutoLoop(false)
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