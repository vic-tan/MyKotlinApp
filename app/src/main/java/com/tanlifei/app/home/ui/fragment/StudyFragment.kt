package com.tanlifei.app.home.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.common.base.ui.fragment.BaseFragment
import com.common.base.ui.fragment.BaseLazyFragment
import com.tanlifei.app.databinding.FragmentHomeBinding
import com.tanlifei.app.databinding.FragmentStudyBinding

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/1/23 17:41
 */
class StudyFragment : BaseFragment<FragmentStudyBinding>() {


    companion object {
        fun newInstance(): StudyFragment {
            val args = Bundle()
            val fragment = StudyFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): FragmentStudyBinding {
        return FragmentStudyBinding.inflate(inflater, container, attachToRoot)
    }




    override fun initView() {
    }


}