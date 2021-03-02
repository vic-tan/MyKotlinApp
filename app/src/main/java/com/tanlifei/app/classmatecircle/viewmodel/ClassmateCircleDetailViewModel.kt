package com.tanlifei.app.classmatecircle.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ObjectUtils
import com.common.core.base.viewmodel.BaseViewModel
import com.tanlifei.app.classmatecircle.bean.ClassmateCircleBean
import com.tanlifei.app.common.network.ApiNetwork

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/7 15:41
 */
class ClassmateCircleDetailViewModel(_id: Long) : BaseViewModel() {

    val id: Long = _id
    var bean: ClassmateCircleBean? = null

    /**
     * 列表数据改变的LveData
     */
    val dataChanged: LiveData<ClassmateCircleBean> get() = _dataChanged
    private var _dataChanged = MutableLiveData<ClassmateCircleBean>()

    fun requestDetail() {
        launchByLoading {
            var requestBean = ApiNetwork.requestEntertainmentDetail(id)
            if (ObjectUtils.isNotEmpty(requestBean)) {
                bean = requestBean
                _dataChanged.value = bean
            }
        }
    }
}