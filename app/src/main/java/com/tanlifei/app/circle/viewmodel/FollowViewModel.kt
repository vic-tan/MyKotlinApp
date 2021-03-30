package com.tanlifei.app.circle.viewmodel

import com.common.base.viewmodel.BaseListViewModel
import com.tanlifei.app.common.repository.Repository

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/7 15:41
 */
class FollowViewModel : BaseListViewModel() {

    override fun requestList() {
        comRequest({
            complete(Repository.requestFriendsEntertainmentList(mPageNum))
        })
    }

}