package com.tanlifei.app.classmatecircle.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.tanlifei.app.classmatecircle.bean.CategoryBean
import com.tanlifei.app.classmatecircle.ui.fragment.RecommendFragment

/**
 * @desc: 为什么不用公用的BasePagerAdapter ，放到List<Fragment>中会导致内存泄漏
 * @author: tanlifei
 * @date: 2021/2/7 13:11
 */
class RecommendTabAdapter(mManager: FragmentManager, private val mCategoryList: List<CategoryBean>) :
    FragmentPagerAdapter(mManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return RecommendFragment.newInstance(mCategoryList[position].categoryId)
    }

    override fun getCount(): Int {
        return mCategoryList.size
    }
}