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

    /**
     * 定时器 常量
     */
    enum class TimerConst(val value: Long) {
        START(-1L),//开始
        COMPLETE(-2L)//完成
    }

    /**
     * 获取验证码类型枚举
     * mobilelogin:手机登录；
     * updatePassword:修改密码；
     * retrievePassword:找回密码
     * unbindPhone:解除绑定
     * modifyPhone:重新绑定
     */
    enum class SMSType(val value: String) {
        MOBILE_LOGIN("mobilelogin"),//手机登录
        UPDATE_PASSWORD("updatePassword"),//修改密码
        RETRIEVE_PASSWORD("retrievePassword"),//找回密码
        UNBIND_PHONE("unbindPhone"),//解除绑定
        MODIFY_PHONE("modifyPhone"),//重新绑定
    }


    /**
     * 分享请求接口的模块CODE类型
     * 模块code
     * 1话题分享2直播专栏分享3文娱图片分享4课程分享5文娱视频分享6自己作业分享7名师分享8直播分享9回播分享10同学的作业
     * 11邀请好友12结课证书13专题分享14电台音频分享15学习周报
     */
    enum class ShareModuleCode(val value: Int) {
        TOPIC(1),//话题分享
        LIVE_COLUMN(2),//直播专栏分享
        CIRCLE_IMAGE(3),//文娱图片分享
        COURSE(4),//课程分享
        CIRCLE_VIDEO(5),//文娱视频分享
        ONESELF_HOMEWORK(6),//自己作业分享
        TEACHER(7),//名师分享
        LIVE(8),//直播分享
        PLAYBACK(9),//回播分享
        CLASSMATE_HOMEWORK(10),//同学的作业
        INVITE_FRIENDS(11),//邀请好友
        CERTIFICATE_COMPLETION(12),//结课证书
        SPECIAL_TOPIC(13),//专题分享
        FM(14),//电台音频分享
        STUDY_WEEKLY(15)//学习周报
    }

}