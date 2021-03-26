package com.common.core.base.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.*
import com.common.cofing.enumconst.UiType
import com.common.core.http.code
import com.common.core.http.msg
import com.common.core.http.show
import com.scwang.smart.refresh.layout.constant.RefreshState
import kotlinx.coroutines.CoroutineScope

/**
 * @desc:ViewModel基类
 * @author: tanlifei
 * @date: 2021/3/24 11:34
 */
open class BaseViewModel : ViewModel() {

    /**
     * 请求网络是否正在加载的LveData
     */
    val mUiChange: LiveData<UiType> get() = uIChange
    private val uIChange = MutableLiveData<UiType>()

    @SuppressLint("StaticFieldLeak")
    lateinit var mApplication: Application

    /**
     * 请求过程状态 (列表中用，滚动过程中加载下一页，当正在加载时，防止重复加载)
     */
    var mRefreshState: RefreshState = RefreshState.None


    /**
     * block 请求成功后处理
     * onError 异常后特殊情况自行处理 默认不处理
     * uiType 接口请求类型 该字段只有列表请求是会用到，其它请求默认为下拉刷新
     * showToast 是否显示异常信息 默认显示
     * uiLiveData 是否要监听请求状态 默认监听
     * refreshState 列表请求时监听请求过程 默认不监听 滚动自动加载下一页时防止过快重重复加载
     */
    open fun comRequest(
        block: suspend CoroutineScope.() -> Unit,
        onError: ((Throwable) -> Unit)? = null,
        showToast: Boolean = true,
        uiLiveData: Boolean = true,
        refreshState: Boolean = false,
        uiType: UiType = UiType.REFRESH
    ) = rxLifeScope.launch(block, {
        if (uiLiveData) {
            uIChange.value = UiType.ERROR
        }
        if (showToast) {
            it.show(it.code, it.msg)
        }
        onError
        if (refreshState) {
            mRefreshState = RefreshState.LoadFinish
        }
    }, {
        if (uiLiveData && uiType == UiType.REFRESH) {
            uIChange.value = UiType.LOADING
        }
        if (refreshState) {
            mRefreshState = RefreshState.Loading
        }
    }, {
        if (uiLiveData) {
            uIChange.value = UiType.COMPLETE
        }
    })

    /**
     * 改变UI类型
     */
    fun setUI(uiType: UiType) {
        uIChange.value = uiType
    }


    companion object {
        @JvmStatic
        fun <T : BaseViewModel> createViewModelFactory(viewModel: T): ViewModelProvider.Factory {
            return ViewModelFactory(viewModel)
        }
    }
}

/**
 * 创建ViewModel的工厂，以此方法创建的ViewModel，可在构造函数中传参
 */
class ViewModelFactory(private val mViewModel: BaseViewModel) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return mViewModel as T
    }
}