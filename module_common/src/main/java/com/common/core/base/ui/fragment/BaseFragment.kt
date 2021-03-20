package com.common.core.base.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.ParameterizedType


/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/1/27 14:49
 */
open abstract class BaseFragment<T : ViewBinding> : Fragment() {
    private var _binding: T? = null
    protected val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
            _binding = inflate.invoke(null, inflater, container, false) as T
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        initBefore()
        return binding.root
    }

    open fun initBefore(){}

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }


    abstract fun initView()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}