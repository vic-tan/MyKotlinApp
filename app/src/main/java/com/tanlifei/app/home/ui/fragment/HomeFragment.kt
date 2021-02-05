package com.tanlifei.app.home.ui.fragment

import android.os.Bundle
import com.common.base.ui.fragment.BaseLazyFragment
import com.tanlifei.app.databinding.FragmentHomeBinding

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/1/23 17:41
 */
class HomeFragment : BaseLazyFragment<FragmentHomeBinding>() {


    companion object {
        fun newInstance(): HomeFragment {
            val args = Bundle()
            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onFirstVisibleToUser() {
        binding.txtBtn.text = "HOME"
    }


}