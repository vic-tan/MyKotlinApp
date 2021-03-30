package com.tanlifei.app.circle.viewmodel

import com.common.base.viewmodel.BaseListViewModel
import com.tanlifei.app.common.repository.Repository

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/7 15:41
 */
class CircleVideoPagerViewModel(private val publishId: Long, private val uid: Long, private val type: Int) :
    BaseListViewModel() {

    override fun requestList() {
        comRequest({
            complete(Repository.requestVideoList(publishId, uid, mPageNum))
        })
    }

}