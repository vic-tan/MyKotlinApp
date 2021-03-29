package com.tanlifei.app.home.ui.fragment

import com.common.core.base.ui.fragment.BaseLazyFragment
import com.common.core.base.viewmodel.EmptyViewModel
import com.tanlifei.app.databinding.FragmentStudyBinding

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/1/23 17:41
 */
class StudyFragment : BaseLazyFragment<FragmentStudyBinding, EmptyViewModel>() {


    companion object {
        fun newInstance() = StudyFragment()
    }

    override fun createViewModel(): EmptyViewModel {
        return EmptyViewModel()
    }

    override fun onFirstVisibleToUser() {
        mBinding.txtBtn.text = "Study..Sophix"
    }


}