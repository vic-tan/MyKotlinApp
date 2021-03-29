package com.common.constant

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/3/29 16:26
 */
interface EnumConst {

    /**
     * 网络请求常用字段 KEY 常量
     */
    enum class UiType {
        LOADING,//加载中
        COMPLETE,//完成
        ERROR,//报错界面

        REFRESH,//下拉刷新（列表中用）
        LOADMORE,//上拉刷新（列表中用）
        NOTIFY,//刷新列表（列表中用）
        CONTENT,//有数据（列表中用）
        REFRESH_CONTENT,//关闭上拉刷新但只有下拉时显示数据（列表中用）
        EMPTY,//无数据（列表中用）
        NO_NEXT,//没有一下页数据（列表中用）
    }

}