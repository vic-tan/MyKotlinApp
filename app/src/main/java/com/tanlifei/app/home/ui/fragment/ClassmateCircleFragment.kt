package com.tanlifei.app.home.ui.fragment

import android.os.Bundle
import com.common.core.base.ui.fragment.BaseLazyFragment
import com.tanlifei.app.databinding.FragmentHomeBinding

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/1/23 17:41
 */
class ClassmateCircleFragment : BaseLazyFragment<FragmentHomeBinding>() {


    companion object {
        fun newInstance(): ClassmateCircleFragment {
            val args = Bundle()
            val fragment = ClassmateCircleFragment()
            fragment.arguments = args
            return fragment
        }
    }



    override fun onFirstVisibleToUser() {
        binding.txtBtn.text = "CLASSMATECIRCLE"
    }



}