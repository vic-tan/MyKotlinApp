package com.tanlifei.app.classmatecircle.viewmodel

import com.common.cofing.enumconst.UiType
import com.common.core.base.viewmodel.BaseListViewModel
import com.tanlifei.app.common.network.ApiNetwork

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/7 15:41
 */
class FollowViewModel : BaseListViewModel() {

    override fun requestList(uiType: UiType) {
        comRequest({
            complete(ApiNetwork.requestFriendsEntertainmentList(mPageNum), uiType)
        }, uiType = uiType)
    }

}