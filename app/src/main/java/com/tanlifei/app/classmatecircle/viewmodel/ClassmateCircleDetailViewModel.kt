package com.tanlifei.app.classmatecircle.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ObjectUtils
import com.common.core.base.viewmodel.BaseListViewModel
import com.tanlifei.app.classmatecircle.bean.ClassmateCircleBean
import com.tanlifei.app.classmatecircle.bean.CommentBean
import com.tanlifei.app.classmatecircle.utils.CommentUrlType
import com.tanlifei.app.common.network.ApiNetwork

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/7 15:41
 */
class ClassmateCircleDetailViewModel(_id: Long) : BaseListViewModel() {

    val id: Long = _id
    var bean: ClassmateCircleBean? = null

    /**
     * 列表数据改变的LveData
     */
    val itemDataChanged: LiveData<Int> get() = _itemDataChanged
    protected var _itemDataChanged = MutableLiveData<Int>()

    private fun requestDetail(dataChangedType: DataChagedType) {
        launchByLoading({
            if (dataChangedType == DataChagedType.REFRESH) {
                var requestBean = ApiNetwork.requestEntertainmentDetail(id)
                if (ObjectUtils.isNotEmpty(requestBean)) {
                    bean = requestBean
                }
            }
            addList(
                ApiNetwork.requestCommentList(id, pageNum),
                dataChangedType
            )
        }, dataChangedType)
    }

    fun requestComment(content: String) {
        launchBySilence {
            var requestBean = ApiNetwork.requestEntertainmentComment(id, content)
            if (ObjectUtils.isNotEmpty(requestBean)) {
                mData.add(0, requestBean)
                _itemDataChanged.value = 0
            }
        }
    }


    override fun requestList(dataChangedType: DataChagedType) {
        requestDetail(dataChangedType)
    }
}