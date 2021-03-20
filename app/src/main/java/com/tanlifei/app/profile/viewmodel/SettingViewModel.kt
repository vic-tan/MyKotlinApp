package com.tanlifei.app.profile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.common.core.base.viewmodel.BaseViewModel
import com.tanlifei.app.common.network.ApiNetwork


/**
 * @desc:设置ViewModel
 * @author: tanlifei
 * @date: 2021/1/28 15:50
 */
class SettingViewModel() : BaseViewModel() {

    /**
     * 登录成功获取到token 的LveData
     */
    val mIsToken: LiveData<Boolean> get() = isToken
    private val isToken = MutableLiveData<Boolean>()


    /**
     * 退出登录
     */
    fun requestLogin() = launchByLoading {
        ApiNetwork.requestLoginOut()
        isToken.value = false
    }


}