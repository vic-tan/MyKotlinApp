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
        fun authPageSuccess(token: String)//唤起授权页成功

        fun fail()

        fun backPressed()

        fun authPageFail()//唤起授权页失败
    }

    /**
     * 自定义一键登录界面回调
     */
    interface UIClickListener {
        fun clickWxBtn() {}
        fun clickEnvironment() {}
        fun clickOtherBtn() {}
    }
}