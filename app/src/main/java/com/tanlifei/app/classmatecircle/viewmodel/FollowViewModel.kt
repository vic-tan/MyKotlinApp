package com.tanlifei.app.classmatecircle.viewmodel

import com.common.core.base.viewmodel.BaseViewModel
import com.common.utils.MyLogTools
import com.tanlifei.app.classmatecircle.network.FollowNetwork

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/7 15:41
 */
class FollowViewModel(private val repository: FollowNetwork) : BaseViewModel() {


    /**
     * 请求短信码
     */
    fun requestFollowList() = launchByLoading {
        var st = repository.requestFriendsEntertainmentList()
        MyLogTools.show(st)
    }
}