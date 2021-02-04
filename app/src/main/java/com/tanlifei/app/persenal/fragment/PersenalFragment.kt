package com.tanlifei.app.persenal.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.common.base.ui.fragment.BaseLazyFragment
import com.tanlifei.app.databinding.FragmentHomeBinding

/**
 * @desc:我的
 * @author: tanlifei
 * @date: 2021/1/23 17:41
 */
class PersenalFragment : BaseLazyFragment<FragmentHomeBinding>() {


    companion object {
        fun newInstance(): PersenalFragment {
            val args = Bundle()
            val fragment = PersenalFragment()
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
        binding.txtBtn.text = "PERSENAL"
    }


}