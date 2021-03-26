package com.tanlifei.app.classmatecircle.viewmodel

import com.common.core.base.viewmodel.BaseListViewModel
import com.tanlifei.app.common.network.Repository

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/7 15:41
 */
class RecommendViewModel(private var mCategoryId: Long = 0) : BaseListViewModel() {


    override fun requestList() {
        comRequest({
            complete(
                Repository.requestFriendsEntertainmentListByType(mPageNum, mCategoryId)
            )
        })
    }

}