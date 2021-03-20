package com.tanlifei.app.home.ui.fragment

import android.os.Bundle
import com.common.core.base.ui.fragment.BaseLazyFragment
import com.tanlifei.app.databinding.FragmentStudyBinding

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/1/23 17:41
 */
class StudyFragment : BaseLazyFragment<FragmentStudyBinding>() {


    companion object {
        fun newInstance(): StudyFragment {
            val fragment = StudyFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }



    override fun onFirstVisibleToUser() {
        mBinding.txtBtn.text = "Study..Sophix"
    }


}