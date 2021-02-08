package com.common.core.base.viewmodel

/**
 * @author: Albert Li
 * @contact: albertlii@163.com
 * @time: 2020/6/9 8:01 PM
 * @description: 页面的常用操作
 * @since: 1.0.0
 */
interface ViewBehavior {
    /**
     * 是否显示Loading视图
     */
    fun showLoadingUI(isShow: Boolean)

    /**
     * 是否显示空白视图
     */
    fun showEmptyUI(isShow: Boolean)

    /**
     * 弹出Toast提示
     */
    fun showToast(map: Map<String, *>)

    /**
     * 不带参数的页面跳转
     */
    fun navigateTo(page: Any)

}