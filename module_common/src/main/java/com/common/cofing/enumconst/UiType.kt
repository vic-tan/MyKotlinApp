package com.common.cofing.enumconst

/**
 * @desc:请求数据通知UI状态类型,
 * @author: tanlifei
 * @date: 2021/3/24 11:25
 */
enum class UiType {
    LOADING,//加载中
    COMPLETE,//完成
    ERROR,//报错界面

    REFRESH,//下拉刷新（列表中用）
    LOADMORE,//上拉刷新（列表中用）
    NOTIFY,//刷新列表（列表中用）
    CONTENT,//有数据（列表中用）
    EMPTY,//无数据（列表中用）
    NO_NEXT,//没有一下页数据（列表中用）
}