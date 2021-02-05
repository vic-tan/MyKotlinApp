package com.tanlifei.app.home.ui.fragment

import android.os.Bundle
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




    override fun onFirstVisibleToUser() {
        binding.txtBtn.text = "CLASS"
    }


}