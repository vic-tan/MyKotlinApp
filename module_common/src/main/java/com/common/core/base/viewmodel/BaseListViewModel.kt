package com.common.core.base.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.common.cofing.constant.GlobalConst
import com.example.httpsender.kt.errorCode
import com.example.httpsender.kt.errorMsg
import com.example.httpsender.kt.show
import com.scwang.smart.refresh.layout.constant.RefreshState

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

    var loadMoreStartPos = 0//用于上接刷新里开始刷新的下标
    var state: RefreshState = RefreshState.None

    /**
     * 列表加载显示处理异常
     */
    protected fun launchByLoading(block: suspend () -> Unit, dataChangedType: DataChagedType) =
        launchByLoading(block, {
            _loadingState.value = LoadType.ERROR
            onError(dataChangedType, it)
        })


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

    fun notifyDataSetChanged(dataChangedType: DataChagedType, startPos: Int = 0) {
        loadMoreStartPos = startPos
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
                DataChagedType.REFRESH -> {
                    mData.clear()
                    mData.addAll(list)
                    notifyDataSetChanged(dataChangedType)
                }
                DataChagedType.LOADMORE -> {
                    val startPos = mData.size - 1
                    mData.addAll(list)
                    notifyDataSetChanged(dataChangedType, startPos)

                }
                else -> {
                    notifyDataSetChanged(dataChangedType)
                }
            }
        }

        if (mData.isEmpty()) {
            showEmptyUI()
        } else {
            showContentUI()
        }
        state = RefreshState.None
    }

    /**
     * 错误处理
     */
    private fun onError(dataChangedType: DataChagedType, it: Throwable) {
        _loadingState.value = LoadType.ERROR
        notifyDataSetChanged(dataChangedType)
        //没有下一页
        if (it.errorCode == GlobalConst.Http.NOT_LOAD_DATA) {
            state = RefreshState.RefreshFinish
            showNotMoreDataUI()
        } else {
            //下拉刷新时才显示错误界面，上拉不处理
            if (dataChangedType == DataChagedType.REFRESH) {
                showErrorUI()
            }
            it.show(it.errorCode, it.errorMsg)
            state = RefreshState.None
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