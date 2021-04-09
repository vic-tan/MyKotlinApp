package com.onlineaginguniversity.circle.viewmodel

import com.common.base.viewmodel.BaseListViewModel
import com.onlineaginguniversity.common.repository.Repository

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/7 15:41
 */
class RecommendViewModel(private val mCategoryId: Long) : BaseListViewModel() {


    override fun requestList() {
        comRequest({
            complete(
                Repository.requestFriendsEntertainmentListByType(mPageNum, mCategoryId)
            )
        })
    }

}