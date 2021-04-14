package com.onlineaginguniversity.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ObjectUtils
import com.common.ComFun
import com.common.base.bean.UpdateAppBean
import com.common.base.viewmodel.BaseViewModel
import com.onlineaginguniversity.common.repository.Repository


/**
 * @desc:应用升级ViewModel
 * @author: tanlifei
 * @date: 2021/1/28 15:50
 */
class UpdateAppViewModel : BaseViewModel() {


    /**
     * 应用升级LveData
     */
    val mUpdateApp: LiveData<UpdateAppBean> get() = updateApp
    private val updateApp = MutableLiveData<UpdateAppBean>()


    /**
     * 应用升级
     */
    fun requestVersion() = comRequest({
        val updateAppBean = Repository.requestVersion()
        if (ObjectUtils.isNotEmpty(updateAppBean)) {
            ComFun.mHandler.postDelayed({
                updateApp.value = updateAppBean
            }, 2000)
        }
    }, uiLiveData = false)
}