package com.tanlifei.app.classmatecircle.viewmodel

import com.common.core.base.viewmodel.BaseListViewModel
import com.tanlifei.app.common.network.ApiNetwork

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/7 15:41
 */
class FollowViewModel() : BaseListViewModel() {


    override fun requestList(dataChangedType: DataChagedType) {
        launchByLoading({
            addList(ApiNetwork.requestFriendsEntertainmentList(pageNum), dataChangedType)
        },dataChangedType)
    }
}