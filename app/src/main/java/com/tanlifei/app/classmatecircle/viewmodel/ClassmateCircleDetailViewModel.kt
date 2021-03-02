package com.tanlifei.app.classmatecircle.viewmodel

import com.blankj.utilcode.util.ObjectUtils
import com.common.core.base.viewmodel.BaseListViewModel
import com.tanlifei.app.classmatecircle.bean.ClassmateCircleBean
import com.tanlifei.app.common.network.ApiNetwork

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/7 15:41
 */
class ClassmateCircleDetailViewModel(_id: Long) : BaseListViewModel() {

    val id: Long = _id
    var bean: ClassmateCircleBean? = null


    private fun requestDetail(dataChangedType: DataChagedType) {
        launchByLoading({
            if (dataChangedType == DataChagedType.REFRESH) {
                var requestBean = ApiNetwork.requestEntertainmentDetail(id)
                if (ObjectUtils.isNotEmpty(requestBean)) {
                    bean = requestBean
                }
            }
            addList(ApiNetwork.requestCommentList(id, pageNum), dataChangedType)
        }, dataChangedType)
    }


    override fun requestList(dataChangedType: DataChagedType) {
        requestDetail(dataChangedType)
    }
}