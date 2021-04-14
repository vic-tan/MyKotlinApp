package com.onlineaginguniversity.login.viewmodel

import android.os.SystemClock
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ObjectUtils
import com.common.base.viewmodel.BaseViewModel
import com.common.widget.component.extension.toast
import com.onlineaginguniversity.common.constant.ApiUrlConst
import com.onlineaginguniversity.common.constant.EnumConst
import com.onlineaginguniversity.common.repository.Repository
import com.onlineaginguniversity.login.bean.PwdLoginResultBean
import com.onlineaginguniversity.login.bean.WxLoginResultBean
import com.onlineaginguniversity.login.utils.LoginUtils
import rxhttp.RxHttp
import rxhttp.toResponse


/**
 * @desc:登录ViewModel
 * @author: tanlifei
 * @date: 2021/1/28 15:50
 */
class LoginViewModel : BaseViewModel() {

    /**
     * 登录成功获取到token 的LveData
     */
    val mIsToken: LiveData<Boolean> get() = this.isToken
    private val isToken = MutableLiveData<Boolean>()
    var mToken: String? = null

    /**
     * 连续点击logo 显示切换环境按钮LveData
     */
    val mIsLogoContinuousClick: LiveData<Boolean> get() = isLogoContinuousClick
    private val isLogoContinuousClick = MutableLiveData<Boolean>()

    /**
     * 密码登录结果
     */
    val mPwdLoginResult: LiveData<PwdLoginResultBean> get() = pwdLoginResult
    private val pwdLoginResult = MutableLiveData<PwdLoginResultBean>()

    /**
     * 微信授权登录结果
     */
    val mWxLoginResult: LiveData<WxLoginResultBean> get() = wxLoginResult
    private val wxLoginResult = MutableLiveData<WxLoginResultBean>()


    /**
     * 微信绑定手机
     */
    val mWxBindPhone: LiveData<Boolean> get() = wxBindPhone
    private val wxBindPhone = MutableLiveData<Boolean>()


    private val mCounts = 10 // 点击次数
    private val mTotalDuration: Long = 10000 // 规定有效时间
    private var mHits: LongArray = LongArray(mCounts)

    /**
     * 倒计时LveData
     */
    val mCodeTimer: LiveData<Long> get() = codeTimer
    private val codeTimer = MutableLiveData<Long>()

    /**
     * 请求短信验证码
     */
    fun requestSmsCode(phone: String, type: EnumConst.SMSType) = comRequest({
        Repository.requestSMSCode(phone, type)
        LoginUtils.startTimer(codeTimer)
    })


    /**
     * 请求发送语音验证码
     */
    fun requestVoiceCode(phone: String, type: EnumConst.SMSType) = comRequest({
        Repository.requestVoiceCode(phone, type)
        toast("请注意接听电话")
    })

    /**
     * 请求短信验证码登录
     */
    fun requestCodeLogin(phone: String, code: String) = comRequest({
        mToken = Repository.requestSMSLogin(phone, code)
        isToken.value = ObjectUtils.isNotEmpty(mToken)
    })

    /**
     * 微信授权登录
     */
    fun requestWechatLogin(openid: String) = comRequest({
        wxLoginResult.value = Repository.requestWechatLogin(openid)
    })

    /**
     * 一键登陆
     */
    fun requestOneKeyLogin(accessToken: String) = comRequest({
        mToken = Repository.requestOneKeyLogin(accessToken)
        isToken.value = ObjectUtils.isNotEmpty(mToken)
    })

    /**
     * 微信绑定用户关系
     */
    fun requestBindPhoneLogin(openid: String) = comRequest({
        Repository.requestBindPhoneLogin(openid)
        wxBindPhone.value = true
    })


    /**
     * 找回密码
     */
    fun requestSetPwd(
        phone: String,
        code: String,
        pwd: String,
        type: EnumConst.SMSType
    ) = comRequest({
        Repository.requestSetPwd(phone, code, pwd, type)
        pwdLoginResult.value = Repository.requestPwdLogin(phone, pwd)
    })


    /**
     * 请求密码登录
     */
    fun requestPwdLogin(phone: String, pwd: String) = comRequest({
        pwdLoginResult.value = Repository.requestPwdLogin(phone, pwd)
    })

    /**
     * 是否连续点击
     */
    fun continuousClick() {
        //每次点击时，数组向前移动一位
        System.arraycopy(mHits, 1, mHits, 0, mHits.size - 1)
        //为数组最后一位赋值
        mHits[mHits.size - 1] = SystemClock.uptimeMillis()
        if (mHits[0] >= SystemClock.uptimeMillis() - mTotalDuration) {
            mHits = LongArray(mCounts) //重新初始化数组
            isLogoContinuousClick.value = true
        }
    }

}