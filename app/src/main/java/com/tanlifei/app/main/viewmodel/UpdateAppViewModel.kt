package com.tanlifei.app.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ObjectUtils
import com.common.core.base.viewmodel.BaseViewModel
import com.common.core.bean.UpdateAppBean
import com.tanlifei.app.common.network.ApiNetwork


/**
 * @desc:应用升级ViewModel
 * @author: tanlifei
 * @date: 2021/1/28 15:50
 */
class UpdateAppViewModel() : BaseViewModel() {


    /**
     * 应用升级LveData
     */
    val updateApp: LiveData<UpdateAppBean> get() = mUpdateApp
    private val mUpdateApp = MutableLiveData<UpdateAppBean>()


    /**
     * 应用升级
     */
    fun requestVersion() = launchBySilence {
        val updateAppBean = ApiNetwork.requestVersion()
        if (ObjectUtils.isNotEmpty(updateAppBean)) {
            mUpdateApp.value = updateAppBean
        }
    }
}