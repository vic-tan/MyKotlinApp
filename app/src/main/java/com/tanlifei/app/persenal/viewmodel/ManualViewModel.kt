package com.tanlifei.app.persenal.viewmodel

import com.common.core.base.viewmodel.BaseListViewModel
import com.tanlifei.app.common.network.ApiNetwork

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/7 15:41
 */
class ManualViewModel() : BaseListViewModel() {

    private val repository: ApiNetwork = ApiNetwork.getInstance()

    override fun requestList(dataChangedType: DataChagedType) {
        launchByLoading({
            addList(repository.requestFriendsEntertainmentList(pageNum), dataChangedType)
        }, {
            onError(dataChangedType,it)
        })
    }
}