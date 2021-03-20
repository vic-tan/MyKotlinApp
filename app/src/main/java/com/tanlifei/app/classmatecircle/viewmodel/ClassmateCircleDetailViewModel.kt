package com.tanlifei.app.classmatecircle.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ObjectUtils
import com.common.core.base.viewmodel.BaseListViewModel
import com.tanlifei.app.classmatecircle.bean.ClassmateCircleBean
import com.tanlifei.app.classmatecircle.bean.CommentBean
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
    private var _itemDataChanged = MutableLiveData<Int>()

    /**
     * 列表数据改变的LveData
     */
    val beanChanged: LiveData<ClassmateCircleBean> get() = _beanChanged
    private var _beanChanged = MutableLiveData<ClassmateCircleBean>()

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


    override fun requestList(dataChangedType: DataChagedType) {
        requestDetail(dataChangedType)
    }

    fun requestComment(content: String) {
        launchBySilence {
            var requestBean = ApiNetwork.requestSendComment(id, content)
            if (ObjectUtils.isNotEmpty(requestBean) && ObjectUtils.isNotEmpty(requestBean.info)) {
                mData.add(0, requestBean.info)
                _itemDataChanged.value = 0
                bean?.comment = bean?.comment?.plus(1)!!
                _beanChanged.value = bean
            }
        }
    }

    fun requestDeleteComment(commentBean: CommentBean) {
        launchBySilence {
            ApiNetwork.requestDeleteComment(commentBean.id)
            mData.remove(commentBean)
            bean?.comment = bean?.comment?.minus(1)!!
            _dataChanged.value = DataChagedType.NOTIFY
            _beanChanged.value = bean
        }
    }


}