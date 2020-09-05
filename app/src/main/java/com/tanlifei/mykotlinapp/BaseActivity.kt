package com.tanlifei.mykotlinapp

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kaopiz.kprogresshud.KProgressHUD
import com.tanlifei.mykotlinapp.core.event.MessageEvent
import com.tanlifei.mykotlinapp.common.utils.ActivityCollector
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.ref.WeakReference

/**
 * 这是activity基类
 * open 表示该类可以被继承 ,kotlin中默认类是不可以被继承
 */
open class BaseActivity: AppCompatActivity() {

    /**
     * 当前Activity的实例。
     */
    protected var activity: Activity? = null//?表示activity 可以为空

    //软引用添加所有的activity
    private var weakRefActivity: WeakReference<Activity>? = null//?weakRefActivity 可以为空


    protected lateinit var hud: KProgressHUD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        weakRefActivity = WeakReference(this)
        ActivityCollector.add(weakRefActivity)
        //事件总线注册
        EventBus.getDefault().register(this)
        hud =  KProgressHUD.create(this)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("正在加载...")
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onMessageEvent(event: MessageEvent) {

    }


    override fun onDestroy() {
        super.onDestroy()
        activity = null
        ActivityCollector.remove(weakRefActivity)
        EventBus.getDefault().unregister(this)
    }

    override fun onStop() {
        super.onStop()
    }



    /**
     * companion 静态
     * object 与companion 同用为伴生对象为单例，原对象没影响。
     */
    companion object {

        //const 可见性为public final static const 必须修饰val const 只允许在top-level级别和object中声明
        private const val TAG = "BaseActivity"
    }
}