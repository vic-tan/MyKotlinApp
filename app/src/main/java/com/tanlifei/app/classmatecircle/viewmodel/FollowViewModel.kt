package com.tanlifei.app.classmatecircle.viewmodel

import com.common.cofing.constant.GlobalConst
import com.common.core.base.viewmodel.BaseListViewModel
import com.common.utils.MyLogTools
import com.example.httpsender.kt.errorCode
import com.tanlifei.app.classmatecircle.bean.ClassmateCircleBean
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
            if (it.errorCode == GlobalConst.Http.NOT_LOAD_DATA) {
                showNotMoreDataUI()
            }
        })
    }


}