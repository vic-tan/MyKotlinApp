package com.common.base.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

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
        _binding = createBinding(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    abstract fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): T

    abstract fun initView()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}