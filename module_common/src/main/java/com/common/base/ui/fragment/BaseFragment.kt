package com.common.base.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.common.base.viewmodel.BaseViewModel
import com.common.core.event.BaseEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.ParameterizedType


/**
 * @desc:Fragment 基类，所以fragment 将继承该类
 * @author: tanlifei
 * @date: 2021/1/27 14:49
 */
open abstract class BaseFragment<V : ViewBinding, VM : BaseViewModel> : Fragment() {
    private var binding: V? = null
    protected val mBinding get() = binding!!
    protected lateinit var mViewModel: VM
    protected abstract fun createViewModel(): VM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (registerEventBus()) {
            EventBus.getDefault().register(this)  //事件总线注册
        }
        val type =
            javaClass.genericSuperclass as ParameterizedType
        val cls = type.actualTypeArguments[0] as Class<*>
        try {
            val inflate = cls.getDeclaredMethod(
                "inflate",
                LayoutInflater::class.java,
                ViewGroup::class.java,
                Boolean::class.javaPrimitiveType
            )
            binding = inflate.invoke(null, inflater, container, false) as V
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        injectViewModel()
        initBefore()
        return mBinding.root
    }


    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun injectViewModel() {
        val vm = createViewModel()
        mViewModel =
            ViewModelProvider(this, BaseViewModel.createViewModelFactory(vm))
                .get(vm::class.java)
        mViewModel.mApplication = requireActivity().application
    }


    /** —————————————————————————————————————————— 子类可重写方法 ————————————————————————————————————————————————— **/

    open fun initBefore() {}

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

    /** —————————————————————————————————————————— 子类抽象方法 ————————————————————————————————————————————————— **/

    abstract fun initView()

    override fun onDestroyView() {
        super.onDestroyView()
        if (registerEventBus()) {
            EventBus.getDefault().unregister(this)
        }
        binding = null
    }
}