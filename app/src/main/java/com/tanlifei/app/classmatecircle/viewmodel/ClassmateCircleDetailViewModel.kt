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
class ClassmateCircleDetailViewModel(val id: Long) : BaseListViewModel() {

    var mBean: ClassmateCircleBean? = null

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
                    mBean = requestBean
                }
            }
            addList(
                ApiNetwork.requestCommentList(id, mPageNum),
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
                mBean?.comment = mBean?.comment?.plus(1)!!
                _beanChanged.value = mBean
            }
        }
    }

    fun requestDeleteComment(commentBean: CommentBean) {
        launchBySilence {
            ApiNetwork.requestDeleteComment(commentBean.id)
            mData.remove(commentBean)
            mBean?.comment = mBean?.comment?.minus(1)!!
            dataChanged.value = DataChagedType.NOTIFY
            _beanChanged.value = mBean
        }
    }


}