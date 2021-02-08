package com.tanlifei.app.classmatecircle.frgment

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.core.base.ui.fragment.BaseBVMFragment
import com.tanlifei.app.classmatecircle.adapter.FollowAdapter
import com.tanlifei.app.classmatecircle.network.FollowNetwork
import com.tanlifei.app.classmatecircle.viewmodel.FollowViewModel
import com.tanlifei.app.databinding.FragmentFollowBinding


/**
 * @desc:同学圈关注
 * @author: tanlifei
 * @date: 2021/1/23 17:41
 */
class FollowFragment : BaseBVMFragment<FragmentFollowBinding, FollowViewModel>() {

    lateinit var adapter: FollowAdapter

    companion object {
        fun newInstance(): FollowFragment {
            val args = Bundle()
            val fragment =
                FollowFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun createViewModel(): FollowViewModel {
        return FollowViewModel(FollowNetwork.getInstance())
    }

    override fun onFirstVisibleToUser() {
        adapter = FollowAdapter(viewModel.mData)
        binding.refreshLayout.refreshRecycler.layoutManager = LinearLayoutManager(activity)
        binding.refreshLayout.refreshRecycler.adapter = adapter
        viewModel.dataChanged.observe(this, Observer {
            adapter.notifyDataSetChanged()
        })
        initSmartRefreshConfig()
        smartRefreshListener()
        viewModel.refresh()
    }


    private fun initSmartRefreshConfig() {
//        binding.refreshLayout.smartRefreshLayout.setEnableLoadMore(false)
//        binding.refreshLayout.smartRefreshLayout.setEnableScrollContentWhenLoaded(false)
//        binding.refreshLayout.smartRefreshLayout.setEnableFooterTranslationContent(false)
    }

    private fun smartRefreshListener() {
        binding.refreshLayout.smartRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
            it.finishRefresh()
        }
        binding.refreshLayout.smartRefreshLayout.setOnLoadMoreListener {
            viewModel.loadMore()
            it.finishLoadMore()
        }
    }

}