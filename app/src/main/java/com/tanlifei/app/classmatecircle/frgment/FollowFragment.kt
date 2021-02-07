package com.tanlifei.app.classmatecircle.frgment

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.common.core.base.ui.fragment.BaseLazyFragment
import com.tanlifei.app.classmatecircle.network.FollowNetwork
import com.tanlifei.app.classmatecircle.viewmodel.FollowViewModel
import com.tanlifei.app.common.bean.BaseViewModel
import com.tanlifei.app.databinding.FragmentFollowBinding

/**
 * @desc:同学圈关注
 * @author: tanlifei
 * @date: 2021/1/23 17:41
 */
class FollowFragment : BaseLazyFragment<FragmentFollowBinding>() {
    private lateinit var viewModel: FollowViewModel

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
        initViewModel()
        viewModel.requestFollowList()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            BaseViewModel.createViewModelFactory(FollowViewModel(FollowNetwork.getInstance()))
        ).get(
            FollowViewModel::class.java
        )
    }

}