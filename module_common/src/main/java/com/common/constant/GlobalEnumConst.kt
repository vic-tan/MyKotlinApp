package com.common.constant

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/3/29 16:26
 */
interface GlobalEnumConst {

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

    /**
     * TextView 设置图片方向常量
     */
    enum class DrawableDirection {
        LEFT,//左
        TOP,//上
        RIGHT,//右
        BOTTOM,//下
    }

    /**
     * 授权类型
     */
    enum class ShareType {
        WECHAT,//微信
        WECHATMOMENTS,//微信朋友圈
        GENERATE_BITMAP,//生成分享图片
        REPORT,//举报
        DELETE,//删除
        OTHER,//其它
    }

    /**
     * 分享UI显示类型
     */
    enum class ShareUIType {
        CIRCLE,//同学圈分享UI（显示举报，隐藏删除）
        CIRCLE_AUTHOR//同学圈分享作者UI（显示生成分享图）
    }
}