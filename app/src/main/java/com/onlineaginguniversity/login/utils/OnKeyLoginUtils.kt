package com.onlineaginguniversity.login.utils

import com.blankj.utilcode.util.ObjectUtils
import com.common.ComFun
import com.mobile.auth.gatewayauth.PhoneNumberAuthHelper
import com.mobile.auth.gatewayauth.ResultCode
import com.mobile.auth.gatewayauth.TokenResultListener
import com.mobile.auth.gatewayauth.model.TokenRet
import com.onlineaginguniversity.common.constant.ComConst
import com.onlineaginguniversity.login.listener.OnKeyLoginListener

/**
 * @desc: 阿里一健登录工具类
 * @author: tanlifei
 * @date: 2021/4/13 17:35
 */
object OnKeyLoginUtils {

    /**
     *  检测终端⽹络环境是否⽀持⼀键登录或者号码认证，根据回调结果确定是否可以使⽤⼀ 键登录功能
     */
    fun checkEnvAvailable(listener: OnKeyLoginListener.CheckEnvAvailable) {
        //2.初始化SDK实例
        var mAliComAuthHelper =
            PhoneNumberAuthHelper.getInstance(ComFun.mContext, object : TokenResultListener {
                override fun onTokenSuccess(s: String?) {
                    ComFun.mHandler.post {
                        var tokenRet: TokenRet?
                        try {
                            tokenRet = TokenRet.fromJson(s)
                            if (ResultCode.CODE_ERROR_ENV_CHECK_SUCCESS == tokenRet.code) {
                                listener.success()
                            } else {
                                listener.failure()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            listener.failure()
                        }


                    }
                }

                override fun onTokenFailed(s: String?) {
                    ComFun.mHandler.post {
                        listener.failure()
                    }
                }
            })
        //3.设置SDK秘钥
        mAliComAuthHelper.setAuthSDKInfo(ComConst.Auth.ALILIBABA_AUTH_SECRET)
        //4.检测终端⽹络环境是否⽀持⼀键登录或者号码认证，根据回调结果确定是否可以使⽤⼀ 键登录功能
        mAliComAuthHelper.checkEnvAvailable(2)
    }

    /**
     *  检测终端⽹络环境是否⽀持⼀键登录或者号码认证，根据回调结果确定是否可以使⽤⼀ 键登录功能
     */
    fun getAuthHelper(
        listener: OnKeyLoginListener.TokenResult
    ): PhoneNumberAuthHelper {
        //初始化SDK实例
        val authHelper =
            PhoneNumberAuthHelper.getInstance(ComFun.mContext, object : TokenResultListener {
                override fun onTokenSuccess(s: String?) {
                    ComFun.mHandler.post {
                        var tokenRet: TokenRet?
                        try {
                            tokenRet = TokenRet.fromJson(s)
                            if (ObjectUtils.isNotEmpty(tokenRet)) {
                                if (ResultCode.CODE_SUCCESS == tokenRet.code) {
                                    listener.success(tokenRet.token)
                                } else {
                                    listener.failure()
                                }
                            } else {
                                listener.failure()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            listener.failure()
                        }
                    }
                }

                override fun onTokenFailed(s: String?) {
                    ComFun.mHandler.post {
                        var tokenRet: TokenRet?
                        try {
                            tokenRet = TokenRet.fromJson(s)
                            if (ObjectUtils.isNotEmpty(tokenRet)) {
                                if (ResultCode.CODE_ERROR_USER_CANCEL == tokenRet!!.code) {
                                    //模拟的是必须登录 否则直接退出app的场景
                                    listener.backPressed()
                                } else {
                                    listener.failure()
                                }
                            } else {
                                listener.failure()
                            }
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                            listener.failure()
                        }
                    }
                }
            })
        authHelper.reporter.setLoggerEnable(true)
        //设置SDK秘钥
        authHelper.setAuthSDKInfo(ComConst.Auth.ALILIBABA_AUTH_SECRET)
        return authHelper
    }


}