package com.onlineaginguniversity.login.listener

/**
 * @desc:阿里一键登录回调
 * @author: tanlifei
 * @date: 2021/4/13 17:48
 */
interface OnKeyLoginListener {
    /**
     * SIM 卡验证回调
     */
    interface CheckEnvAvailable {
        fun success()

        fun failure()
    }

    /**
     * SIM 卡验证回调
     */
    interface TokenResult {
        fun success(token:String)

        fun failure()

        fun backPressed()
    }

    /**
     * 自定义一键登录界面回调
     */
    interface UIClickListener {
        fun clickWxBtn() {}
        fun clickEnvironment() {}
    }
}