package com.tanlifei.app.home.ui.fragment

import android.os.Bundle
import com.common.core.base.ui.fragment.BaseLazyFragment
import com.tanlifei.app.databinding.FragmentHomeBinding
import com.tanlifei.app.databinding.FragmentStudyBinding

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/1/23 17:41
 */
class ClassFragment : BaseLazyFragment<FragmentStudyBinding>() {

    companion object {
        fun newInstance(): ClassFragment {
            val fragment = ClassFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onFirstVisibleToUser() {
        binding.txtBtn.text = "Class"
    }


}