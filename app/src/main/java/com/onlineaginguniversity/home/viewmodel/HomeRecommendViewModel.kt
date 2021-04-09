package com.onlineaginguniversity.home.viewmodel

import com.common.base.viewmodel.BaseListViewModel
import com.onlineaginguniversity.common.repository.Repository

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/7 15:41
 */
class HomeRecommendViewModel(private var categoryId: Long = 1) : BaseListViewModel() {


    override fun requestList() {
        comRequest({
            complete(
                Repository.requestFriendsEntertainmentListByType(mPageNum, categoryId)
            )
        })
    }

}