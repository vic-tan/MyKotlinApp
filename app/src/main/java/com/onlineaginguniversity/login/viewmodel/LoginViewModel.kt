package com.onlineaginguniversity.login.viewmodel

import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ObjectUtils
import com.common.base.viewmodel.BaseViewModel
import com.onlineaginguniversity.common.repository.Repository
import com.onlineaginguniversity.login.utils.LoginUtils


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

    private val mCounts = 10 // 点击次数
    private val mTotalDuration: Long = 10000 // 规定有效时间
    var mHits: LongArray = LongArray(mCounts)

    /**
     * 倒计时LveData
     */
    val mCodeTimer: LiveData<Long> get() = codeTimer
    private val codeTimer = MutableLiveData<Long>()

    /**
     * 请求短信验证码
     */
    fun requestSmsCode(phone: String) = comRequest({
        Repository.requestSMSCode(phone)
        LoginUtils.startTimer(codeTimer)
    })


    /**
     * 请求发送语音验证码
     */
    fun requestVoiceCode(phone: String) = comRequest({
        Repository.requestVoiceCode(phone)
    })

    /**
     * 请求登录
     */
    fun requestCodeLogin(phone: String, code: String) = comRequest({
        mToken = Repository.requestSMSLogin(phone, code)
        isToken.value = ObjectUtils.isNotEmpty(mToken)
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