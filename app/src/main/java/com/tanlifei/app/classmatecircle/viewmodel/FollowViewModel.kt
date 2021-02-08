package com.tanlifei.app.classmatecircle.viewmodel

import com.common.core.base.viewmodel.BaseListViewModel
import com.tanlifei.app.classmatecircle.network.FollowNetwork

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/7 15:41
 */
class FollowViewModel(private val repository: FollowNetwork) : BaseListViewModel() {

    override fun requestList(dataChangedType: DataChagedType) {
        launchByLoading({
            addList(repository.requestFriendsEntertainmentList(pageNum), dataChangedType)
        }, {
            onError(dataChangedType,it)
        })
    }
}