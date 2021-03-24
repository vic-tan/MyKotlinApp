package com.tanlifei.app.classmatecircle.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ObjectUtils
import com.common.cofing.enumconst.UiType
import com.common.core.base.bean.ListDataChangePrams
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
    val mItemDataChanged: LiveData<Int> get() = itemDataChanged
    private var itemDataChanged = MutableLiveData<Int>()

    /**
     * 列表数据改变的LveData
     */
    val mBeanChanged: LiveData<ClassmateCircleBean> get() = beanChanged
    private var beanChanged = MutableLiveData<ClassmateCircleBean>()

    private fun requestDetail() =
        comRequest({
            if (mRefreshType == UiType.REFRESH) {
                var requestBean = ApiNetwork.requestEntertainmentDetail(id)
                if (ObjectUtils.isNotEmpty(requestBean)) {
                    mBean = requestBean
                }
            }
            complete(
                ApiNetwork.requestCommentList(id, mPageNum)
            )
        })


    override fun requestList() {
        requestDetail()
    }

    fun requestComment(content: String) = comRequest({
        var requestBean = ApiNetwork.requestSendComment(id, content)
        if (ObjectUtils.isNotEmpty(requestBean) && ObjectUtils.isNotEmpty(requestBean.info)) {
            mData.add(0, requestBean.info)
            itemDataChanged.value = 0
            mBean?.comment = mBean?.comment?.plus(1)!!
            beanChanged.value = mBean
        }
    }, uiLiveData = false)

    fun requestDeleteComment(commentBean: CommentBean) = comRequest({
        ApiNetwork.requestDeleteComment(commentBean.id)
        mData.remove(commentBean)
        mBean?.comment = mBean?.comment?.minus(1)!!
        setDataChange(ListDataChangePrams(UiType.NOTIFY))
        beanChanged.value = mBean
    }, uiLiveData = false)


}