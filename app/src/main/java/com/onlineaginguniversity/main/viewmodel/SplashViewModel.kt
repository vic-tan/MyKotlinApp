package com.onlineaginguniversity.main.viewmodel

import android.graphics.drawable.Drawable
import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.SPUtils
import com.common.ComFun
import com.common.base.viewmodel.BaseViewModel
import com.common.widget.component.extension.log
import com.onlineaginguniversity.common.constant.ComConst
import com.onlineaginguniversity.common.repository.Repository
import com.onlineaginguniversity.common.utils.UserInfoUtils
import com.onlineaginguniversity.main.bean.AdsBean
import com.onlineaginguniversity.main.utils.AdsUtils
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
class SplashViewModel : BaseViewModel() {

    enum class JumpType {
        GUIDE,//表示去引导页
        LOGIN,//登录界面
        HOME,//首页
        REQUEST_ADS, //广告界面（接口请求完成
        ADS//广告界面（启动界面完成）
    }


    /**
     * 3s结束倒计时LveData
     */
    val mJump: LiveData<JumpType> get() = jump
    private val jump = MutableLiveData<JumpType>()
    var splashTimer: CountDownTimer? = null

    /**
     * 广告倒计时LveData
     */
    val mAdsInterval: LiveData<Long> get() = adsInterval
    private val adsInterval = MutableLiveData<Long>()

    var mAdsBean: AdsBean? = null

    /**
     * 广告图片加载完成
     */
    var adsDrawable: Drawable? = null

    /**
     * 请求广告图片
     */
    fun requestAds() = comRequest({
        mAdsBean = Repository.requestAds()
        if (ObjectUtils.isNotEmpty(mAdsBean)) {
            LitePal.deleteAll(AdsBean::class.java)
            mAdsBean!!.save()
            jump.value = JumpType.REQUEST_ADS
        }
    }, uiLiveData = false, onError = {
        findAdsByDB()
    })

    /**
     * 查找数据库中是否保存广告
     */
    private fun findAdsByDB() {
        if (ObjectUtils.isEmpty(mAdsBean)) {
            mAdsBean = AdsUtils.getAds()
            if (ObjectUtils.isNotEmpty(mAdsBean)) {
                jump.value = JumpType.REQUEST_ADS
            }
        }
    }

    fun stopSplashTimerAdsDrawable(drawable: Drawable?) {
        if (ObjectUtils.isNotEmpty(drawable)) {
            adsDrawable = drawable
            stopSplashTimer()
        }
    }

    /**
     * 启动页3s倒计时
     */
    fun splashTimer(count: Long = 3) {
        if (ObjectUtils.isEmpty(splashTimer)) {
            splashTimer = object : CountDownTimer(count * 1000, 1000) {
                override fun onFinish() {
                    doJump()
                }

                override fun onTick(millisUntilFinished: Long) {}
            }
        }
        splashTimer?.start()
    }


    /**
     * 启动页倒计时条件
     * 不是第一次启动app，且广告图片加载完成
     */
    private fun stopSplashTimer() {
        if (!ObjectUtils.isEmpty(adsDrawable) && !SPUtils.getInstance()
                .getBoolean(ComConst.SPKey.GUIDE, true)
        ) {
            splashTimer?.let {
                it.cancel()
            }
            doJump()
        }
    }


    /**
     * 跳转到指定activity
     */
    fun doJump() {
        var guide = SPUtils.getInstance().getBoolean(ComConst.SPKey.GUIDE, true)
        if (guide) {
            jump.value = JumpType.GUIDE
        } else {
            findAdsByDB()
            if (ObjectUtils.isNotEmpty(mAdsBean) && (ObjectUtils.isNotEmpty(mAdsBean) && mAdsBean?.status == 1)) {
                jump.value = JumpType.ADS
            } else {
                val token = UserInfoUtils.getToken()
                //已经登录过了
                if (ObjectUtils.isNotEmpty(token)) {
                    ComFun.mToken = token
                    jump.value = JumpType.HOME
                } else {//未登录
                    jump.value = JumpType.LOGIN
                }
            }
        }
    }


    /**
     * 启动页3s倒计时
     */
    fun startAdsTimer() {
        var count = 3
        if (ObjectUtils.isNotEmpty(mAdsBean)) {
            count = mAdsBean!!.duration
        }
        Observable.interval(0, 1, TimeUnit.SECONDS)
            .take((count + 1).toLong())
            .map { aLong -> count - aLong }
            .observeOn(AndroidSchedulers.mainThread()) //ui线程中进行控件更新
            .doOnSubscribe {}.subscribe(object : Observer<Long> {
                override fun onSubscribe(d: Disposable?) {}
                override fun onNext(t: Long) {
                    adsInterval.value = t//倒计时
                }

                override fun onError(e: Throwable?) {}
                override fun onComplete() {
                    adsInterval.value = -1L//回复原来初始状态
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
            ComFun.mToken = token
            jump.value = JumpType.HOME
        } else {//未登录
            jump.value = JumpType.LOGIN
        }
    }
}