package com.onlineaginguniversity.main.ui.activity

import com.common.base.ui.activity.BaseActivity
import com.common.base.viewmodel.EmptyViewModel
import com.common.widget.component.extension.startActivity
import com.onlineaginguniversity.R
import com.onlineaginguniversity.databinding.ActivityGuideBinding
import com.onlineaginguniversity.main.adapter.GuideAdapter
import com.youth.banner.indicator.CircleIndicator


/**
 * @desc: 引导界面
 * @author: tanlifei
 * @date: 2021/1/22 16:26
 */
class GuideActivity : BaseActivity<ActivityGuideBinding, EmptyViewModel>() {

    var mGuideList: MutableList<Int> = ArrayList()

    companion object {
        fun actionStart() {
            startActivity<GuideActivity> { }
        }
    }

    override fun createViewModel(): EmptyViewModel {
        return EmptyViewModel()
    }

    override fun showFullScreen(): Boolean {
        return true
    }


    override fun init() {
        addGuideData()
        mBinding.banner.adapter = GuideAdapter(this, mGuideList)
        mBinding.banner.indicator = CircleIndicator(this)
        mBinding.banner.isAutoLoop(false)
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