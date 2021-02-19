package com.tanlifei.app.persenal.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ObjectUtils
import com.common.core.base.viewmodel.BaseListViewModel
import com.tanlifei.app.common.bean.ManualBean
import com.tanlifei.app.common.bean.UserBean
import com.tanlifei.app.common.network.ApiNetwork

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/7 15:41
 */
class ManualViewModel() : BaseListViewModel() {

    private val repository: ApiNetwork = ApiNetwork.getInstance()

    /**
     * 详情
     */
    val bean: LiveData<ManualBean> get() = _bean
    private val _bean = MutableLiveData<ManualBean>()

    override fun requestList(dataChangedType: DataChagedType) {
        launchByLoading({
            addList(repository.requestManualList(pageNum), dataChangedType)
        }, dataChangedType)
    }

    fun requestManualDetail(manualId: Long) {
        launchByLoading {
            var manualBean = repository.requestManualDetail(manualId)
            if (ObjectUtils.isNotEmpty(manualBean)) {
                _bean.value = manualBean
            }
        }
    }
}