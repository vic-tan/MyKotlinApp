package com.tanlifei.app.classmatecircle.viewmodel

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ObjectUtils
import com.common.core.base.viewmodel.BaseViewModel
import com.tanlifei.app.classmatecircle.adapter.RecommendTabAdapter
import com.tanlifei.app.classmatecircle.bean.CategoryBean
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
    val dataChanged: LiveData<Boolean> get() = mDataChanged
    private var mDataChanged = MutableLiveData<Boolean>()

    lateinit var tabAdapter: RecommendTabAdapter

    fun requestCategoryList() {
        launchByLoading {
            val categoryList = ApiNetwork.requestEntertainmentCategoryList()
            if (ObjectUtils.isNotEmpty(categoryList)) {
                mData.addAll(categoryList)
                addFragment()
            } else {
                mLoadingState.value = LoadType.ERROR
            }
        }
    }

    private fun addFragment() {
        tabAdapter = RecommendTabAdapter(manager, mData)
        mDataChanged.value = true
    }
}