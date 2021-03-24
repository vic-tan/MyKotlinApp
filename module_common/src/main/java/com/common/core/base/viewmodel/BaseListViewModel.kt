package com.common.core.base.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.common.cofing.enumconst.UiType
import com.common.core.base.bean.ListDataChangePrams
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

/**
 * @desc:列表加载
 * @author: tanlifei
 * @date: 2021/2/8 9:31
 */
open abstract class BaseListViewModel : BaseViewModel() {

    /** 列表数据 **/
    var mData: MutableList<Any> = mutableListOf()

    /** 分页下标 **/
    var mPageNum: Int = 1

    /** 请求接口返回数据结果的LveData **/
    val mDataChange: LiveData<ListDataChangePrams> get() = dataChange
    private var dataChange = MutableLiveData<ListDataChangePrams>()

    /**
     * 请求过程状态 (列表中用，滚动过程中加载下一页，当正在加载时，防止重复加载)
     */
    var mRefreshType: UiType = UiType.REFRESH

    /**
     * 请求列表接口
     */
    abstract fun requestList()


    /**
     * 列表通用请求
     */
    override fun comRequest(
        block: suspend CoroutineScope.() -> Unit,
        onError: ((Throwable) -> Unit)?,
        showToast: Boolean,
        uiLiveData: Boolean,
        refreshState: Boolean,
        uiType: UiType
    ): Job {
        return super.comRequest(
            block,
            onError = onError,
            showToast = showToast,
            uiLiveData = uiLiveData,
            refreshState = refreshState,
            uiType = mRefreshType
        )
    }

    /**
     * 改变数据类型
     */
    fun setDataChange(listDataChangePrams: ListDataChangePrams) {
        if(mData.isEmpty()) {
            setUI(UiType.EMPTY)
        }
        dataChange.value = listDataChangePrams
    }

    /**
     * 刷新
     */
    fun refresh() {
        mPageNum = 1
        mRefreshType = UiType.REFRESH
        requestList()
    }

    /**
     * 加载更多
     */
    fun loadMore() {
        mRefreshType = UiType.LOADMORE
        requestList()
    }

    /**
     * 加载完成
     */
    fun complete(resultList: List<Any>) {
        when (mRefreshType) {
            UiType.REFRESH -> {//下拉刷新
                mData.clear()
                if (resultList.isNotEmpty()) {
                    mData.addAll(resultList)
                    setUI(UiType.CONTENT)//有数据
                    dataChange.value = ListDataChangePrams(mRefreshType, resultList.size)
                } else {
                    setUI(UiType.EMPTY)//无数据
                    dataChange.value = ListDataChangePrams(mRefreshType)
                }
            }
            UiType.LOADMORE -> {//上拉加载更多
                if (resultList.isNotEmpty()) {
                    mData.addAll(resultList)
                    setUI(UiType.COMPLETE)//加载
                    dataChange.value = ListDataChangePrams(mRefreshType, resultList.size)
                } else {
                    setUI(UiType.NO_NEXT)//没有一下页数据
                }
            }
        }
        mPageNum++
    }


}