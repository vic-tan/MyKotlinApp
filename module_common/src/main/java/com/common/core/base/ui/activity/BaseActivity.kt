package com.common.core.base.ui.activity

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.ObjectUtils
import com.common.R
import com.common.cofing.enumconst.UiType
import com.common.core.base.event.BaseEvent
import com.common.core.base.viewmodel.BaseViewModel
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
open abstract class BaseActivity<V : ViewBinding, VM : BaseViewModel> : AppCompatActivity(),
    Observer<UiType> {

    lateinit var mBaseBinding: ActivityBaseBinding
    lateinit var mBinding: V
    lateinit var mTitleBar: TitleBar

    lateinit var mViewModel: VM

    /**
     * 当前Activity的实例。
     */
    private var activity: Activity? = null
    protected val mActivity get() = activity!!

    //加载框
    lateinit var mHud: BasePopupView


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
        injectViewModel()
        initBefore()
        init()
    }


    /**
     * 工厂创建viewModel
     */
    private fun injectViewModel() {
        val vm = createViewModel()
        mViewModel =
            ViewModelProvider(this, BaseViewModel.createViewModelFactory(createViewModel()))
                .get(vm::class.java)
        mViewModel.mApplication = application
    }


    /**
     * 初始化View 通过反射加载viewbinding
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

    /** ——————————————————————————————————————————start 软键盘的处理 ————————————————————————————————————————————————— **/

    /**
     * 清除editText的焦点
     *
     * @param v   焦点所在View
     * @param views 输入框
     */
    private fun clearViewFocus(v: View?, views: MutableList<View>) {
        if (null != v && null != views && views.isNotEmpty()) {
            for (view in views) {
                if (v == view) {
                    v.clearFocus()
                    break
                }
            }
        }
    }

    /**
     * 隐藏键盘
     *
     * @param v   焦点所在View
     * @param views 输入框
     * @return true代表焦点在edit上
     */
    private fun isFocusEditText(v: View?, views: MutableList<View>): Boolean {
        if (v is EditText) {
            for (view in views) {
                if (v == view) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * 是否触摸在指定view上面,对某个控件过滤
     *
     * @param views
     * @param ev
     * @return
     */
    private fun isTouchView(views: MutableList<View>?, ev: MotionEvent): Boolean {
        if (views == null || views.isEmpty()) {
            return false
        }
        val location = IntArray(2)
        for (view in views) {
            view.getLocationOnScreen(location)
            val x = location[0]
            val y = location[1]
            if (ev.x > x && ev.x < x + view.width && ev.y > y && ev.y < y + view.height
            ) {
                return true
            }
        }
        return false
    }

    /**
     * 是否触摸在指定view上面,对某个控件过滤
     *
     * @param ids
     * @param ev
     * @return
     */
    private fun isTouchView(ids: IntArray, ev: MotionEvent): Boolean {
        val location = IntArray(2)
        for (id in ids) {
            val view: View = findViewById(id) ?: continue
            view.getLocationOnScreen(location)
            val x = location[0]
            val y = location[1]
            if (ev.x > x && ev.x < x + view.width && ev.y > y && ev.y < y + view.height
            ) {
                return true
            }
        }
        return false
    }

    /**
     *
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        try {
            if (ev.action == MotionEvent.ACTION_DOWN) {
                if (isTouchView(filterViewByIds(), ev)) {
                    return super.dispatchTouchEvent(ev)
                }
                if (showSoftByEditView() == null || showSoftByEditView().isEmpty()) {
                    return super.dispatchTouchEvent(ev)
                }
                val v = currentFocus
                if (isFocusEditText(v, showSoftByEditView())) {
                    if (isTouchView(showSoftByEditView(), ev)) {
                        return super.dispatchTouchEvent(ev)
                    }
                    //隐藏键盘
                    KeyboardUtils.hideSoftInput(this)
                    clearViewFocus(v, showSoftByEditView())
                }
            }
        } finally {
            return super.dispatchTouchEvent(ev)
        }
    }


    /**
     * 传入要过滤的View
     * 过滤之后点击将不会有隐藏软键盘的操作
     *
     * @return view 数组
     */
    open fun filterViewByIds(): MutableList<View> {
        return mutableListOf()
    }

    /**
     * 传入EditText的Id
     * 没有传入的EditText不做处理
     *
     * @return view 数组
     */
    open fun showSoftByEditView(): MutableList<View> {
        return mutableListOf()
    }

    /** —————————————————————————————————————————— 子类可重写方法 ————————————————————————————————————————————————— **/


    /**
     *  设置 registerEventBus 为true后重写该方法
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onMessageEvent(event: BaseEvent) {

    }

    /**
     * 是否注册EventBus
     */
    open fun registerEventBus(): Boolean {
        return false
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
     * 初始化之前方法
     */
    open fun initBefore() {

    }


    /**
     * 加载框显示
     */
    override fun onChanged(uiType: UiType) {
        loadingView(uiType)
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

    /** —————————————————————————————————————————— 子类抽象方法 ————————————————————————————————————————————————— **/


    /**
     * viewModel 实例化
     */
    protected abstract fun createViewModel(): VM

    /**
     * 初始化View
     */
    abstract fun init()


}