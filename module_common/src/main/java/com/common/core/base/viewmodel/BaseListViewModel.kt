package com.common.core.base.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/8 9:31
 */
open class BaseListViewModel : BaseViewModel() {

    enum class DataChagedType {
        NOTIFY,//数据改变刷新
    }

    /**
     * 列表数据改变的LveData
     */
    val dataChanged: LiveData<DataChagedType> get() = _dataChanged
    protected var _dataChanged = MutableLiveData<DataChagedType>()

    fun notifyDataSetChanged() {
        _dataChanged.value = DataChagedType.NOTIFY
    }
}