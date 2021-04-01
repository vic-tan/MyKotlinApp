package com.tanlifei.app.home.ui.fragment

import com.common.base.ui.fragment.BaseLazyFragment
import com.common.base.viewmodel.EmptyViewModel
import com.tanlifei.app.databinding.FragmentStudyBinding

/**
 * @desc:
 * @author: tanlifei
 * @date: 2021/1/23 17:41
 */
class StudyFragment : BaseLazyFragment<FragmentStudyBinding, EmptyViewModel>() {


    override fun createViewModel(): EmptyViewModel {
        return EmptyViewModel()
    }

    override fun onFirstVisibleToUser() {
        mBinding.txtBtn.text = "Study..Sophix"
    }


}