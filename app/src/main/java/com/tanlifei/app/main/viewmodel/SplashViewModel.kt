package com.tanlifei.app.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.SPUtils
import com.common.ComApplication
import com.common.core.base.viewmodel.BaseViewModel
import com.tanlifei.app.common.config.Const
import com.tanlifei.app.common.network.ApiNetwork
import com.tanlifei.app.common.utils.UserInfoUtils
import com.tanlifei.app.main.bean.AdsBean
import com.tanlifei.app.main.utils.AdsUtils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import org.litepal.LitePal
import java.util.concurrent.TimeUnit


/**
 * @desc:启动页ViewModel
 * @author: tanlifei
 * @date: 2021/1/28 15:50
 */
 class SplashViewModel() : BaseViewModel() {

    enum class JumpType {
        GUIDE,//表示去引导页
        LOGIN,//登录界面
        HOME,//首页
        REQUEST_ADS, //广告界面（接口请求完成
        ADS//广告界面（启动界面完成）
    }

    private val repository: ApiNetwork = ApiNetwork.getInstance()

    /**
     * 3s结束倒计时LveData
     */
    val jump: LiveData<JumpType> get() = _jump
    private val _jump = MutableLiveData<JumpType>()


    /**
     * 广告倒计时LveData
     */
    val adsInterval: LiveData<Long> get() = _adsInterval
    private val _adsInterval = MutableLiveData<Long>()

    var adsBean: AdsBean? = null


    /**
     * 请求短信码
     */
    fun requestAds() = launchBySilence({
        adsBean = repository.requestAds()
        if (ObjectUtils.isNotEmpty(adsBean)) {
            LitePal.deleteAll(AdsBean::class.java)
            adsBean!!.save()
            _jump.value = JumpType.REQUEST_ADS
        }
    }, {
        findAdsByDB()
    })

    /**
     * 查找数据库中是否保存广告
     */
    private fun findAdsByDB() {
        if (ObjectUtils.isEmpty(adsBean)) {
            adsBean = AdsUtils.getAds()
            if (ObjectUtils.isNotEmpty(adsBean)) {
                _jump.value = JumpType.REQUEST_ADS
            }
        }
    }

    /**
     * 启动页3s倒计时
     */
    fun startInterval(count: Long = 3) {
        Observable.interval(0, 1, TimeUnit.SECONDS)
            .take((count + 1).toLong())
            .map { aLong -> count - aLong }
            .observeOn(AndroidSchedulers.mainThread()) //ui线程中进行控件更新
            .doOnSubscribe {}.subscribe(object : Observer<Long> {
                override fun onSubscribe(d: Disposable?) {}
                override fun onNext(t: Long) {}
                override fun onError(e: Throwable?) {}
                override fun onComplete() {
                    doJump()
                }
            })
    }

    /**
     * 跳转到指定activity
     */
    fun doJump() {
        var guide = SPUtils.getInstance().getBoolean(Const.SPKey.GUIDE, true)
        if (guide) {
            _jump.value = JumpType.GUIDE
        } else {
            findAdsByDB()
            if (ObjectUtils.isNotEmpty(adsBean) && (ObjectUtils.isNotEmpty(adsBean) && adsBean?.status == 1)) {
                _jump.value = JumpType.ADS
            } else {
                val token = UserInfoUtils.getToken()
                //已经登录过了
                if (ObjectUtils.isNotEmpty(token)) {
                    ComApplication.token = token
                    _jump.value = JumpType.HOME
                } else {//未登录
                    _jump.value = JumpType.LOGIN
                }
            }
        }
    }


    /**
     * 启动页3s倒计时
     */
    fun startAdsInterval() {
        var count = 3
        if (ObjectUtils.isNotEmpty(adsBean)) {
            count = adsBean!!.duration
        }
        Observable.interval(0, 1, TimeUnit.SECONDS)
            .take((count + 1).toLong())
            .map { aLong -> count - aLong }
            .observeOn(AndroidSchedulers.mainThread()) //ui线程中进行控件更新
            .doOnSubscribe {}.subscribe(object : Observer<Long> {
                override fun onSubscribe(d: Disposable?) {}
                override fun onNext(t: Long) {
                    _adsInterval.value = t//倒计时
                }

                override fun onError(e: Throwable?) {}
                override fun onComplete() {
                    _adsInterval.value = -1L//回复原来初始状态
                }
            })
    }

    /**
     * 跳转到指定activity
     */
    fun doAdsJump() {
        val token = UserInfoUtils.getToken()
        //已经登录过了
        if (ObjectUtils.isNotEmpty(token)) {
            ComApplication.token = token
            _jump.value = JumpType.HOME
        } else {//未登录
            _jump.value = JumpType.LOGIN
        }
    }
}