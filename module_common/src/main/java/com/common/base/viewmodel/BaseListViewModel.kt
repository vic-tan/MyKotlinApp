package com.common.base.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.common.base.bean.ListDataChangePrams
import com.common.constant.EnumConst
import com.scwang.smart.refresh.layout.constant.RefreshState
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
    var mRefreshType: EnumConst.UiType = EnumConst.UiType.REFRESH

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
        uiType: EnumConst.UiType
    ): Job {
        return super.comRequest(
            block,
            onError = onError,
            showToast = showToast,
            uiLiveData = uiLiveData,
            refreshState = true,
            uiType = mRefreshType
        )
    }

    /**
     * 改变数据类型
     */
    fun setDataChange(listDataChangePrams: ListDataChangePrams) {
        if (mData.isEmpty()) {
            setUI(EnumConst.UiType.EMPTY)
        }
        dataChange.value = listDataChangePrams
    }

    /**
     * 刷新
     */
    fun refresh() {
        mPageNum = 1
        mRefreshType = EnumConst.UiType.REFRESH
        requestList()
    }

    /**
     * 加载更多
     */
    fun loadMore() {
        mRefreshType = EnumConst.UiType.LOADMORE
        requestList()
    }

    /**
     * 加载完成
     * @isMoreRefresh 是否开启上拉加载更多，默认是可以上拉刷新的
     */
    fun complete(resultList: List<Any>, isMoreRefresh: Boolean = true) {
        when (mRefreshType) {
            EnumConst.UiType.REFRESH -> {//下拉刷新
                mData.clear()
                if (resultList.isNotEmpty()) {
                    mData.addAll(resultList)
                    setUI(if (isMoreRefresh) EnumConst.UiType.CONTENT else EnumConst.UiType.REFRESH_CONTENT)//有数据
                    dataChange.value = ListDataChangePrams(mRefreshType, resultList.size)
                    mRefreshState = RefreshState.None
                } else {
                    setUI(EnumConst.UiType.EMPTY)//无数据
                    dataChange.value = ListDataChangePrams(mRefreshType)
                }
            }
            EnumConst.UiType.LOADMORE -> {//上拉加载更多
                if (resultList.isNotEmpty()) {
                    mData.addAll(resultList)
                    setUI(EnumConst.UiType.COMPLETE)//加载
                    dataChange.value = ListDataChangePrams(mRefreshType, resultList.size)
                } else {
                    if (mData.isEmpty()) {
                        setUI(EnumConst.UiType.EMPTY)//无数据
                    } else {
                        setUI(EnumConst.UiType.NO_NEXT)//没有一下页数据
                    }
                }
            }
        }
        mPageNum++
    }


}