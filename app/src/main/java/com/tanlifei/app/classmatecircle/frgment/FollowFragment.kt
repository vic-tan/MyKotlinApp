package com.tanlifei.app.classmatecircle.frgment

import android.os.Bundle
import com.common.core.base.ui.fragment.BaseBVMFragment
import com.tanlifei.app.classmatecircle.network.FollowNetwork
import com.tanlifei.app.classmatecircle.viewmodel.FollowViewModel
import com.tanlifei.app.databinding.FragmentFollowBinding

/**
 * @desc:同学圈关注
 * @author: tanlifei
 * @date: 2021/1/23 17:41
 */
class FollowFragment : BaseBVMFragment<FragmentFollowBinding, FollowViewModel>() {

    companion object {
        fun newInstance(): FollowFragment {
            val args = Bundle()
            val fragment =
                FollowFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onFirstVisibleToUser() {
        viewModel.requestFollowList()
    }


    override fun createViewModel(): FollowViewModel {
        return FollowViewModel(FollowNetwork.getInstance())
    }

}