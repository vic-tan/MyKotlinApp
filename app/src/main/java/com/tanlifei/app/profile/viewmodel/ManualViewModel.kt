package com.tanlifei.app.profile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ObjectUtils
import com.common.core.base.viewmodel.BaseListViewModel
import com.tanlifei.app.common.network.ApiNetwork
import com.tanlifei.app.profile.bean.ManualBean

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/7 15:41
 */
class ManualViewModel() : BaseListViewModel() {


    /**
     * 详情
     */
    val mBean: LiveData<ManualBean> get() = bean
    private val bean = MutableLiveData<ManualBean>()

    override fun requestList(dataChangedType: DataChagedType) {
        launchByLoading({
            addList(ApiNetwork.requestManualList(mPageNum), dataChangedType)
        }, dataChangedType)
    }

    fun requestManualDetail(manualId: Long) {
        launchByLoading {
            var manualBean = ApiNetwork.requestManualDetail(manualId)
            if (ObjectUtils.isNotEmpty(manualBean)) {
                bean.value = manualBean
            }
        }
    }
}