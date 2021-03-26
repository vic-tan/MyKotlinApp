package com.common.core.base.ui.activity

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.ObjectUtils
import com.common.R
import com.common.cofing.enumconst.UiType
import com.common.core.base.event.BaseEvent
import com.common.databinding.ActivityBaseBinding
import com.common.utils.extension.gone
import com.common.utils.extension.setVisible
import com.common.widget.popup.CustomLoadingView
import com.gyf.immersionbar.ktx.immersionBar
import com.hjq.bar.TitleBar
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.enums.PopupAnimation
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType


/**
 * 这是activity基类
 * open 表示该类可以被继承 ,kotlin中默认类是不可以被继承
 */
open abstract class BaseActivity<V : ViewBinding> : AppCompatActivity(),
    Observer<UiType> {

    protected lateinit var mBaseBinding: ActivityBaseBinding
    protected lateinit var mBinding: V
    protected lateinit var mTitleBar: TitleBar


    /**
     * 当前Activity的实例。
     */
    private var activity: Activity? = null
    protected val mActivity get() = activity!!

    //    protected lateinit var hud: KProgressHUD//加载框
    protected lateinit var mHud: BasePopupView//加载框


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        mBaseBinding = ActivityBaseBinding.inflate(layoutInflater)
        mTitleBar = mBaseBinding.toolbar
        mTitleBar.setLeftIcon(R.mipmap.ic_arrow_left_black)
        mHud = XPopup.Builder(this)
            .popupAnimation(PopupAnimation.NoAnimation)
            .hasShadowBg(false)
            .dismissOnBackPressed(false) // 按返回键是否关闭弹窗，默认为true
            .dismissOnTouchOutside(false) // 点击外部是否关闭弹窗，默认为true
            .asCustom(CustomLoadingView(this))
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
        setContentView(mBaseBinding.root)
        initLayout()
        setToolbar(false)
        setOrientation()
        initImmersionBar()
        initBefore()
        init()
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
        mBaseBinding.statusBarView.gone()
        immersionBar() {
            statusBarDarkFont(true, 0.2f)
        }
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

    open fun setToolbar(visible: Boolean) {
        mTitleBar.setVisible(visible)
    }

    /**
     * 是否显示加载框
     */
    open fun loadingView(uiType: UiType) {
        if (ObjectUtils.isNotEmpty(mHud)) {
            when (uiType) {
                UiType.LOADING -> if (mHud.isDismiss) mHud.show()
                UiType.COMPLETE -> if (mHud.isShow) mHud.dismiss()
                else -> {
                }
            }
        }
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
        val type: ParameterizedType = javaClass.genericSuperclass as ParameterizedType
        val cls = type.actualTypeArguments[0] as Class<*>
        try {
            val inflate: Method = cls.getDeclaredMethod("inflate", LayoutInflater::class.java)
            mBinding = inflate.invoke(null, layoutInflater) as V
            mBaseBinding.baseContainer.addView(mBinding.root)
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onMessageEvent(event: BaseEvent) {

    }


    override fun onDestroy() {
        super.onDestroy()
        if (null != mHud && mHud.isShow) {
            mHud.dismiss()
        }
        activity = null
        if (registerEventBus()) {
            EventBus.getDefault().unregister(this)
        }

    }


    open fun initBefore() {

    }

    /**
     * 初始化View
     */
    abstract fun init()


    /**
     * companion 静态
     * object 与companion 同用为伴生对象为单例，原对象没影响。
     */
    companion object {
        //const 可见性为public final static const 必须修饰val const 只允许在top-level级别和object中声明
        private const val TAG = "BaseActivity"
    }

    /**
     * 加载框显示
     */
    override fun onChanged(uiType: UiType) {
        loadingView(uiType)
    }
}