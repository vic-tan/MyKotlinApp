package com.common.core.base.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.*
import com.example.httpsender.kt.errorCode
import com.example.httpsender.kt.errorMsg
import com.example.httpsender.kt.show

/**
 * @desc:ViewModel基类
 * @author: tanlifei
 * @date: 2021/1/28 15:55
 */
open class BaseViewModel : ViewModel() {

    /**
     * UI加载框状态显示
     */
    enum class LoadType {
        LOADING,//加载中
        DISMISS,//完成
        ERROR,//报错界面
    }

    /**
     * 请求网络是否正在加载的LveData
     */
    val mLoadingState: LiveData<LoadType> get() = loadingState
    protected val loadingState = MutableLiveData<LoadType>()

    @SuppressLint("StaticFieldLeak")
    lateinit var mApplication: Application

    /**
     * 加载框请求不需要处理异常，直接提示异常
     */
    protected fun launchByLoading(block: suspend () -> Unit) = launchByLoading(block, {
        loadingState.value = LoadType.ERROR
        it.show(it.errorCode, it.errorMsg)
    })


    /**
     * 加载框请求，需要自己处理异常，不提示异常
     */
    protected fun launchByLoading(
        block: suspend () -> Unit,
        onError: ((Throwable) -> Unit)? = null
    ) = rxLifeScope.launch({
        loadingState.value = LoadType.LOADING
        block()
        loadingState.value = LoadType.DISMISS
    }, onError, {
        loadingState.value = LoadType.LOADING
    })


    /**
     * 静默加载
     */
    protected fun launchBySilence(
        block: suspend () -> Unit,
        onError: ((Throwable) -> Unit)? = null
    ) = rxLifeScope.launch({ block() }, onError)

    /**
     * 静默加载请求不需要处理异常，直接提示异常
     */
    protected fun launchBySilence(block: suspend () -> Unit) = launchBySilence(block, {
        it.show(it.errorCode, it.errorMsg)
    })

    /**
     * 由于屏幕旋转导致的Activity重建，该方法不会被调用
     *
     * 只有ViewModel已经没有任何Activity与之有关联，系统则会调用该方法，你可以在此清理资源
     */
    override fun onCleared() {
        super.onCleared()
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