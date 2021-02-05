package com.common.core.base.ui.fragment

import android.os.Bundle
import androidx.viewbinding.ViewBinding

/**
 * @desc: 懒加载Fragment
 * @author: tanlifei
 * @date: 2021/1/27 15:05
 */
open abstract class BaseLazyFragment<T : ViewBinding> : BaseFragment<T>() {

    private var isFirstVisible: Boolean = true
    private var isPrepared: Boolean = false


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initPrepare()
    }

    override fun onResume() {
        super.onResume()
        if (isFirstVisible) {
            initPrepare()
            isFirstVisible = false
        } else {
            onVisibleToUser()
        }
    }

    override fun onPause() {
        super.onPause()
        onInvisibleToUser()
    }


    @Synchronized
    private fun initPrepare() {
        if (isPrepared) {
            onFirstVisibleToUser()
        } else {
            isPrepared = true
        }
    }


    override fun initView() {
        
    }

    /**
     * 第一次显示出来（用户第一次看到时）
     */
    protected abstract fun onFirstVisibleToUser()

    /**
     * 每次显示出来（除去第一次）
     */
    protected open fun onVisibleToUser() {

    }

    /**
     * 每次隐藏时
     */
    protected open fun onInvisibleToUser() {

    }

}