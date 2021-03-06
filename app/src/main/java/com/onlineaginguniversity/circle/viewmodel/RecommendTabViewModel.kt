package com.onlineaginguniversity.circle.viewmodel

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ObjectUtils
import com.common.base.viewmodel.BaseViewModel
import com.common.constant.GlobalEnumConst
import com.onlineaginguniversity.circle.adapter.RecommendTabAdapter
import com.onlineaginguniversity.circle.bean.CategoryBean
import com.onlineaginguniversity.common.repository.Repository
import java.util.*

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/7 15:41
 */
class RecommendTabViewModel(private val mManager: FragmentManager) : BaseViewModel() {

    var mData: MutableList<CategoryBean> = ArrayList()

    /**
     * 列表数据改变的LveData
     */
    val mDataChanged: LiveData<Boolean> get() = dataChanged
    private var dataChanged = MutableLiveData<Boolean>()

    lateinit var mTabAdapter: RecommendTabAdapter

    fun requestCategoryList() {
        comRequest({
            val categoryList = Repository.requestEntertainmentCategoryList()
            if (ObjectUtils.isNotEmpty(categoryList)) {
                mData.addAll(categoryList)
                addFragment()
            } else {
                setUI(GlobalEnumConst.UiType.ERROR)
            }
        })
    }

    private fun addFragment() {
        mTabAdapter = RecommendTabAdapter(mManager, mData)
        dataChanged.value = true
    }
}