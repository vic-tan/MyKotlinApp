package com.onlineaginguniversity.common.constant

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/3/29 16:26
 */
interface EnumConst {

    /**
     * 首页TAB 标识 常量
     */
    enum class HomeTabTag(val value: Int) {
        HOME(0),//首页
        CLASS(1),//班级
        CIRCLE(2),//同学圈
        STUDY(3),//学习
        PROFILE(4)//我的
    }

    /**
     * 同学圈TAB 标识 常量
     */
    enum class CircleTabTag(val value: Int, val title: String) {
        CIRCLE(0, "关注"),//关注
        RECOMMEND(1, "推荐")//推荐
    }

    /**
     * 链接跳转类型常量
     */
    enum class LinkJumpType(val value: String) {
        TYPE_COMMON("common"),//通用
        TYPE_GAME("game"),//游戏
        TYPE_SHOP_JIUJIU("shop_jiujiu"),//九九商城
        TYPE_SHOP_TRAVEL("shop_travel"),//旅游商城
        TYPE_WECHAT_MINI_WSLNDX("wechat_mini_wslndx"),//网大小程序
        TYPE_WECHAT_MINI_SHOP("wechat_mini_shop"),//电商小程序
        TYPE_LIVE_COLUMN_FREE("live_column_free"),//免费课程
        TYPE_LIVE_COLUMN_CHARGE("live_column_charge"),//付费课程
        TYPE_LIVE_NOT_JUMP("not_jump"),//不跳转
        TYPE_WECHAT_MINI_JIUJIU_BBS("wechat_mini_jiujiu_bbs"),//论坛


    }

}