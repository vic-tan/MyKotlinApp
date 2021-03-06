package com.tanlifei.app.classmatecircle.viewmodel

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ObjectUtils
import com.common.core.base.adapter.BasePagerAdapter
import com.common.core.base.viewmodel.BaseViewModel
import com.example.httpsender.kt.errorCode
import com.example.httpsender.kt.errorMsg
import com.example.httpsender.kt.show
import com.tanlifei.app.classmatecircle.bean.CategoryBean
import com.tanlifei.app.classmatecircle.ui.fragment.RecommendFragment
import com.tanlifei.app.common.network.ApiNetwork
import java.util.*

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/7 15:41
 */
class RecommendTabViewModel(private val manager: FragmentManager) : BaseViewModel() {

    var mData: MutableList<CategoryBean> = ArrayList()

    /**
     * 列表数据改变的LveData
     */
    val dataChanged: LiveData<Boolean> get() = _dataChanged
    private var _dataChanged = MutableLiveData<Boolean>()

    lateinit var fragmentAdapter: BasePagerAdapter
    private var mFragments: MutableList<Fragment> = ArrayList()

    fun requestCategoryList() {
        launchByLoading{
            val categoryList = ApiNetwork.requestEntertainmentCategoryList()
            if (ObjectUtils.isNotEmpty(categoryList)) {
                mData.addAll(categoryList)
                addFragment()
            } else {
                _loadingState.value = LoadType.ERROR
            }
        }
    }

    private fun addFragment() {
        if (ObjectUtils.isNotEmpty(mData)) {
            for (categoryBean in mData) {
                mFragments.add(RecommendFragment.newInstance(categoryBean.categoryId))
            }
        }
        fragmentAdapter = BasePagerAdapter(manager, mFragments)
        _dataChanged.value = true
    }
}