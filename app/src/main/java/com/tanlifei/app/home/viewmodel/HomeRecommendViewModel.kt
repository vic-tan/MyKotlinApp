package com.tanlifei.app.home.viewmodel

import com.common.core.base.viewmodel.BaseListViewModel
import com.tanlifei.app.common.network.ApiNetwork

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/7 15:41
 */
class HomeRecommendViewModel(private var categoryId: Long = 1) : BaseListViewModel() {


    override fun requestList(dataChangedType: DataChagedType) {
        launchByLoading({
            addList(
                ApiNetwork.requestFriendsEntertainmentListByType(mPageNum, categoryId),
                dataChangedType
            )
        }, dataChangedType)
    }

}