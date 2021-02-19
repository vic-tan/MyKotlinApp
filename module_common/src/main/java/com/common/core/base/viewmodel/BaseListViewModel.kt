package com.common.core.base.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.common.cofing.constant.GlobalConst
import com.example.httpsender.kt.errorCode

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
        CONTENT,//有内容
        LOADING,//加载中
        EMPTYDATA,//空列表
        NOTMOREDATA,//没有一下页数据
        ERROR,//报错界面
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
        } else {
            showContentUI()
        }
    }

    /**
     * 错误处理
     */
    fun onError(dataChangedType: DataChagedType, it: Throwable) {
        _isLoading.value = false
        notifyDataSetChanged(dataChangedType)
        //没有下一页
        if (it.errorCode == GlobalConst.Http.NOT_LOAD_DATA) {
            showNotMoreDataUI()
        } else {
            //下拉刷新时才显示错误界面，上拉不处理
            if (dataChangedType == DataChagedType.REFRESH) {
                showErrorUI()
            }
        }
    }

    override fun showLoadingUI() {
        _uiBehavior.value = UIType.LOADING
    }

    override fun showEmptyUI() {
        _uiBehavior.value = UIType.EMPTYDATA
    }


    override fun showContentUI() {
        _uiBehavior.value = UIType.CONTENT
    }

    override fun showNotMoreDataUI() {
        _uiBehavior.value = UIType.NOTMOREDATA
    }

    override fun showErrorUI() {
        _uiBehavior.value = UIType.ERROR
    }


    abstract fun requestList(dataChangedType: DataChagedType)


}