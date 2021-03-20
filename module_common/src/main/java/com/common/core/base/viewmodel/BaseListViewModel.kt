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
        ERROE,//请求报错时不刷新数据，但上下拉要关闭
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

    var mLoadMoreStartPos = 0//用于上接刷新里开始刷新的下标
    var mState: RefreshState = RefreshState.None

    /**
     * 列表加载显示处理异常
     */
    protected fun launchByLoading(block: suspend () -> Unit, dataChangedType: DataChagedType) =
        launchByLoading(block, {
            loadingState.value = LoadType.ERROR
            onError(dataChangedType, it)
        })


    /**
     * 显示Ui 的LveData
     */
    val mUiBehavior: LiveData<UIType> get() = uiBehavior
    private val uiBehavior = MutableLiveData<UIType>()

    var mData: MutableList<Any> = mutableListOf()
    var mPageNum: Int = 1

    /**
     * 列表数据改变的LveData
     */
    val mDataChanged: LiveData<DataChagedType> get() = dataChanged
    protected var dataChanged = MutableLiveData<DataChagedType>()

    fun notifyDataSetChanged(dataChangedType: DataChagedType, startPos: Int = 0) {
        mLoadMoreStartPos = startPos
        dataChanged.value = dataChangedType
    }

    /**
     * 下拉刷新
     */
    fun refresh() {
        mPageNum = 1
        requestList(DataChagedType.REFRESH)
    }

    /**
     * 加载更多
     */
    fun loadMore() {
        requestList(DataChagedType.LOADMORE)
    }

    fun addList(list: List<Any>, dataChangedType: DataChagedType) {
        when (dataChangedType) {
            DataChagedType.REFRESH -> {
                mData.clear()
                if (list.isNotEmpty()) {
                    mData.addAll(list)
                }
                notifyDataSetChanged(dataChangedType)
            }
            DataChagedType.LOADMORE -> {
                val startPos = mData.size - 1
                if (list.isNotEmpty()) {
                    mData.addAll(list)
                }
                notifyDataSetChanged(dataChangedType, startPos)
            }
            else -> {
                notifyDataSetChanged(dataChangedType)
            }
        }

        if (mData.isEmpty()) {
            showEmptyUI()
        } else {
            showContentUI()
        }
        mState = RefreshState.None
        mPageNum++
    }

    /**
     * 错误处理
     */
    private fun onError(dataChangedType: DataChagedType, it: Throwable) {
        loadingState.value = LoadType.ERROR
        //没有下一页
        if (it.errorCode == GlobalConst.Http.NOT_LOAD_DATA) {
            mState = RefreshState.RefreshFinish
            showNotMoreDataUI()
            notifyDataSetChanged(dataChangedType, mData.size - 1)
        } else {
            //下拉刷新时才显示错误界面，上拉不处理
            if (dataChangedType == DataChagedType.REFRESH) {
                showErrorUI()
            }
            it.show(it.errorCode, it.errorMsg)
            mState = RefreshState.None
            notifyDataSetChanged(DataChagedType.ERROE)
        }

    }

    override fun showLoadingUI() {
        uiBehavior.value = UIType.LOADING
    }

    override fun showEmptyUI() {
        uiBehavior.value = UIType.EMPTYDATA
    }


    override fun showContentUI() {
        uiBehavior.value = UIType.CONTENT
    }

    override fun showNotMoreDataUI() {
        uiBehavior.value = UIType.NOTMOREDATA
    }

    override fun showErrorUI() {
        uiBehavior.value = UIType.ERROR
    }


    abstract fun requestList(dataChangedType: DataChagedType)


}