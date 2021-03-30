package com.tanlifei.app.common.constant

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

}