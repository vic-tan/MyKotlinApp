package com.common.core.base.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * @desc:列表加载
 * @author: tanlifei
 * @date: 2021/2/8 9:31
 */
open abstract class BaseListViewModel : BaseViewModel(), ViewBehavior {

    enum class DataChagedType {
        REFRESH,//下拉改变刷新
        LOADMORE,//上拉加载刷新
        NOTIFY,//数据改变刷新
    }

    enum class UIType {
        REFRESH,//下拉改变刷新
        LOADMORE,//上拉加载刷新
        LOADING,//加载中
        EMPTYDATA,//空列表
        NOTMOREDATA,//没有一下页数据
    }


    /**
     * 显示Ui 的LveData
     */
    val uiBehavior: LiveData<UIType> get() = _uiBehavior
    private val _uiBehavior = MutableLiveData<UIType>()

    var mData: MutableList<Any> = ArrayList()
    var pageNum: Int = 0

    /**
     * 列表数据改变的LveData
     */
    val dataChanged: LiveData<DataChagedType> get() = _dataChanged
    protected var _dataChanged = MutableLiveData<DataChagedType>()

    fun notifyDataSetChanged(dataChangedType: DataChagedType) {
        _dataChanged.value = dataChangedType
    }

    /**
     * 下拉刷新
     */
    fun refresh() {
        pageNum = 1
        requestList(DataChagedType.REFRESH)
    }

    /**
     * 加载更多
     */
    fun loadMore() {
        pageNum++
        requestList(DataChagedType.LOADMORE)
    }

    fun addList(list: List<Any>, dataChangedType: DataChagedType) {
        if (list.isNotEmpty()) {
            when (dataChangedType) {
                DataChagedType.REFRESH -> mData.clear()
            }
            if (list.isNotEmpty()) {
                mData.addAll(list)
            }
        }
        notifyDataSetChanged(dataChangedType)
        if (mData.isEmpty()) {
            showEmptyUI()
        }
    }

    override fun showLoadingUI() {
        _uiBehavior.value = UIType.LOADING
    }

    override fun showEmptyUI() {
        _uiBehavior.value = UIType.EMPTYDATA
    }


    override fun showNotMoreDataUI() {
        _uiBehavior.value = UIType.NOTMOREDATA
    }


    abstract fun requestList(dataChangedType: DataChagedType)


}