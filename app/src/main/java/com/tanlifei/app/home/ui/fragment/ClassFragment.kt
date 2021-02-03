package com.tanlifei.app.home.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.common.base.ui.fragment.BaseLazyFragment
import com.tanlifei.app.databinding.FragmentHomeBinding

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/1/23 17:41
 */
class ClassFragment : BaseLazyFragment<FragmentHomeBinding>() {

    companion object {
        fun newInstance(): ClassFragment {
            val args = Bundle()
            val fragment = ClassFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, attachToRoot)
    }


    override fun onFirstVisibleToUser() {
        binding.txtBtn.text = "CLASS"
    }


}