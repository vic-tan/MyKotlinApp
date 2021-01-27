package com.tanlifei.mykotlinapp.common.activity

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.gyf.immersionbar.ktx.immersionBar
import com.hjq.bar.TitleBar
import com.kaopiz.kprogresshud.KProgressHUD
import com.tanlifei.mykotlinapp.core.event.MessageEvent
import com.tanlifei.mykotlinapp.databinding.ActivityBaseBinding
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * 这是activity基类
 * open 表示该类可以被继承 ,kotlin中默认类是不可以被继承
 */
open abstract class BaseActivity<T : ViewBinding>  : AppCompatActivity() {

    private lateinit var baseBinding: ActivityBaseBinding
    protected lateinit var binding: T
    private lateinit var titleBar: TitleBar


    /**
     * 当前Activity的实例。
     */
    private var mActivity: Activity? = null//?表示activity 可以为空

    private lateinit var hud: KProgressHUD//加载框


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = this
        baseBinding = ActivityBaseBinding.inflate(layoutInflater)
        titleBar = baseBinding.toolbar
        hud = KProgressHUD.create(this)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("正在加载...")
            .setCancellable(true)
            .setAnimationSpeed(2)
            .setDimAmount(0.5f)
        initSet()
    }

    /**
     * 初始化
     */
    private fun initSet() {
        if (registerEventBus()) {
            EventBus.getDefault().register(this)  //事件总线注册
        }
        setLayoutBeforeParams()
        if (showFullScreen()) {
            setFullScreen()
        }
        setContentView(baseBinding.root)
        initLayout()
        setToolbar(View.GONE)
        setOrientation()
        initImmersionBar()
        initView()
    }

    /**
     * 在setContentView 设置前设置一些参数
     */
    open fun setLayoutBeforeParams() {
    }

    /**
     * 沉浸式
     */
    open fun initImmersionBar() {
        immersionBar()
    }

    /**
     * 设置横竖屏
     */
    open fun setOrientation() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    /**
     * 是否显示状态栏
     */

    open fun setToolbar(visibility: Int) {
        titleBar.visibility = visibility
    }


    /**
     * 是否显示全屏
     */
    open fun showFullScreen(): Boolean {
        return false
    }

    /**
     * 全屏显示
     */
    open fun setFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    /**
     * 是否注册EventBus
     */
    open fun registerEventBus(): Boolean {
        return false
    }

    /**
     * 初始化View
     */
    private fun initLayout() {
        if (layoutResId() != -1) {
            var view = View.inflate(this, layoutResId(), null)
            binding = createBinding(view)
            baseBinding.container.addView(view.rootView)
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onMessageEvent(event: MessageEvent) {

    }


    override fun onDestroy() {
        super.onDestroy()
        mActivity = null
        if (registerEventBus()) {
            EventBus.getDefault().unregister(this)
        }
    }


    /**
     * 设置布局文件
     */
    abstract fun layoutResId(): Int

    /**
     * 绑定binding
     */
    abstract fun createBinding(layoutView: View): T



    /**
     * 初始化View
     */
    abstract fun initView()


    /**
     * companion 静态
     * object 与companion 同用为伴生对象为单例，原对象没影响。
     */
    companion object {
        //const 可见性为public final static const 必须修饰val const 只允许在top-level级别和object中声明
        private const val TAG = "BaseActivity"
    }
}