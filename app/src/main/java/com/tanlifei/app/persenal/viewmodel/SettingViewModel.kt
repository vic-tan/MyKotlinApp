package com.tanlifei.app.persenal.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.common.core.base.viewmodel.BaseViewModel
import com.tanlifei.app.persenal.network.SettingNetwork


/**
 * @desc:设置ViewModel
 * @author: tanlifei
 * @date: 2021/1/28 15:50
 */
class SettingViewModel(private val repository: SettingNetwork) : BaseViewModel() {

    /**
     * 登录成功获取到token 的LveData
     */
    val isToken: LiveData<Boolean> get() = _isToken
    private val _isToken = MutableLiveData<Boolean>()


    /**
     * 退出登录
     */
    fun requestLogin() = launchByLoading {
        repository.requestLoginOut()
        _isToken.value = false
    }


}