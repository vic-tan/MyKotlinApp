package com.tanlifei.app.classmatecircle.viewmodel

import com.common.core.base.viewmodel.BaseListViewModel
import com.common.core.base.viewmodel.BaseViewModel
import com.common.utils.MyLogTools
import com.tanlifei.app.classmatecircle.bean.ClassmateCircleBean
import com.tanlifei.app.classmatecircle.network.FollowNetwork

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/7 15:41
 */
class FollowViewModel(private val repository: FollowNetwork) : BaseListViewModel() {

    var mData: MutableList<ClassmateCircleBean> = ArrayList()
    var pageNum: Int = 0

    /**
     * 请求短信码
     */
    private fun requestFollowList() = launchByLoading {
        var lt = repository.requestFriendsEntertainmentList(pageNum)
        if (lt.isNotEmpty()) {
            mData.addAll(lt)
        }
        notifyDataSetChanged()
    }

    /**
     * 下拉刷新
     */
    fun refresh() {
        pageNum = 1
        mData.clear()
        requestFollowList()
    }

    /**
     * 加载更多
     */
    fun loadMore() {
        pageNum++
        requestFollowList()
    }

}