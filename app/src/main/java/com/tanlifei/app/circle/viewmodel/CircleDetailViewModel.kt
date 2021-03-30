package com.tanlifei.app.circle.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ObjectUtils
import com.common.base.bean.ListDataChangePrams
import com.common.base.viewmodel.BaseListViewModel
import com.common.constant.GlobalEnumConst
import com.tanlifei.app.circle.bean.CircleBean
import com.tanlifei.app.circle.bean.CommentBean
import com.tanlifei.app.circle.bean.ImageBean
import com.tanlifei.app.common.repository.Repository

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/2/7 15:41
 */
class CircleDetailViewModel(val id: Long) : BaseListViewModel() {

    var mBean: CircleBean? = null

    var mBannerData: MutableList<ImageBean> = mutableListOf()

    /**
     * 列表数据改变的LveData
     */
    val mItemDataChanged: LiveData<Int> get() = itemDataChanged
    private var itemDataChanged = MutableLiveData<Int>()

    /**
     * 列表数据改变的LveData
     */
    val mBeanChanged: LiveData<CircleBean> get() = beanChanged
    private var beanChanged = MutableLiveData<CircleBean>()

    private fun requestDetail() =
        comRequest({
            if (mRefreshType == GlobalEnumConst.UiType.REFRESH) {
                var requestBean = Repository.requestEntertainmentDetail(id)
                if (ObjectUtils.isNotEmpty(requestBean)) {
                    mBean = requestBean
                    mBannerData.clear()
                    if (ObjectUtils.isNotEmpty(mBean)) {
                        mBean!!.imagesUrlList?.let { mBannerData.addAll(it) }
                    }
                }
            }
            complete(
                Repository.requestCommentList(id, mPageNum)
            )
        })


    override fun requestList() {
        requestDetail()
    }

    fun requestComment(content: String) = comRequest({
        var requestBean = Repository.requestSendComment(id, content)
        if (ObjectUtils.isNotEmpty(requestBean) && ObjectUtils.isNotEmpty(requestBean.info)) {
            mData.add(0, requestBean.info)
            itemDataChanged.value = 0
            mBean?.comment = mBean?.comment?.plus(1)!!
            beanChanged.value = mBean
        }
    }, uiLiveData = false)

    fun requestDeleteComment(commentBean: CommentBean) = comRequest({
        Repository.requestDeleteComment(commentBean.id)
        mData.remove(commentBean)
        mBean?.comment = mBean?.comment?.minus(1)!!
        setDataChange(ListDataChangePrams(GlobalEnumConst.UiType.NOTIFY))
        beanChanged.value = mBean
    }, uiLiveData = false)


}