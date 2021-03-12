package com.tanlifei.app.classmatecircle.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.tanlifei.app.classmatecircle.bean.CategoryBean
import com.tanlifei.app.classmatecircle.ui.fragment.RecommendFragment

/**
 * @desc:Viewpage
 * @author: tanlifei
 * @date: 2021/2/7 13:11
 */
class RecommendTabAdapter(manager: FragmentManager, private val categoryList: List<CategoryBean>) :
    FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return RecommendFragment.newInstance(categoryList[position].categoryId)
    }

    override fun getCount(): Int {
        return categoryList.size
    }
}