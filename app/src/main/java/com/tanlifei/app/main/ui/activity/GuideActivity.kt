package com.tanlifei.app.main.ui.activity

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

    var mGuideList: MutableList<Int> = ArrayList()

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
        binding.banner.adapter = GuideAdapter(this, mGuideList)
        binding.banner.indicator = CircleIndicator(this)
        binding.banner.isAutoLoop(false)
    }

    private fun addGuideData() {
        mGuideList.add(R.mipmap.bg_guide_one)
        mGuideList.add(R.mipmap.bg_guide_two)
        mGuideList.add(R.mipmap.bg_guide_three)
        mGuideList.add(R.mipmap.bg_guide_four)
    }

    /**
     * 不允许返回
     */
    override fun onBackPressed() {

    }
}